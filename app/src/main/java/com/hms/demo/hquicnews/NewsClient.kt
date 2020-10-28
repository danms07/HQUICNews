package com.hms.demo.hquicnews

import android.content.Context
import android.util.Log
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo
import org.json.JSONObject
import java.net.URLDecoder
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class NewsClient(context: Context): UrlRequest.Callback() {
    var hquicService: HQUICService? = null
    val CAPACITY = 10240
    val TAG="NewsDownloader"
    var response:StringBuilder=java.lang.StringBuilder()
    var listener:NewsClientListener?=null

    init {
        hquicService = HQUICService(context)
        hquicService?.setCallback(this)
    }


    fun getNews(url: String, method:String){
        hquicService?.sendRequest(url,method)
    }

    override fun onRedirectReceived(
        request: UrlRequest,
        info: UrlResponseInfo,
        newLocationUrl: String
    ) {
        request.followRedirect()
    }

    override fun onResponseStarted(request: UrlRequest, info: UrlResponseInfo) {
        Log.i(TAG, "onResponseStarted: ")
        val byteBuffer = ByteBuffer.allocateDirect(CAPACITY)
        request.read(byteBuffer)

    }

    override fun onReadCompleted(
        request: UrlRequest,
        info: UrlResponseInfo,
        byteBuffer: ByteBuffer
    ) {
        Log.i(TAG, "onReadCompleted: method is called")
        val readed=String(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.position())
        response.append(readed)
        Log.e("Response", readed)
        request.read(ByteBuffer.allocateDirect(CAPACITY))
    }

    override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {
        //If everything is ok you can read the response body
        val json=JSONObject(response.toString())
        val array=json.getJSONArray("articles")
        val list=ArrayList<Article>()
        for (i in 0 until array.length()){
            val article=array.getJSONObject(i)
            val author=article.getString("author")
            val title=article.getString("title")
            val description=article.getString("description")
            val time=article.getString("publishedAt")
            val url=article.getString("url")
            list.add(Article(author, title, description, url, time))
        }
        listener?.onSuccess(list)
    }

    override fun onFailed(request: UrlRequest, info: UrlResponseInfo, error: CronetException) {
        //If someting fails you must report the error
        listener?.onFailure(error.toString())
    }

    public interface NewsClientListener{
        fun onSuccess(news:ArrayList<Article>)
        fun onFailure(error: String)
    }
}