package com.hms.demo.hquicnews

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.collections.ArrayList

class NewsViewModel : ViewModel(), NewsClient.NewsClientListener {
    companion object{
        private const val API_KEY="ac0805f2465f4f89853f569ad67085cb" //get your API key here: https://newsapi.org/register
        const val URL = "https://newsapi.org/v2/top-headlines?apiKey=$API_KEY"
        const val METHOD = "GET"
    }

    var listener: NewsViewModelListener?=null
    var newsClient:NewsClient?=null

    private val _news=MutableLiveData<ArrayList<Article>>().apply{
        value= ArrayList()
    }
    val news: LiveData<ArrayList<Article>> = _news


    public fun getNews(context: Context,country:String=Locale.getDefault().country) {
        if(newsClient==null){
            newsClient=NewsClient(context)
        }
        val url= "$URL&country=$country"
        Log.e("url",url)
        newsClient?.apply {
            listener=this@NewsViewModel
            getNews(url,METHOD)
        }
    }

    public fun loadNews(context: Context){
        news.value?.apply {
            if(isEmpty()) getNews(context)
            else listener?.onNewsDownloadComplete()
        }
    }

    override fun onSuccess(news: ArrayList<Article>) {
        _news.postValue(news)
        listener?.onNewsDownloadComplete()
    }

    override fun onFailure(error: String) {
        listener?.onNewsDownloadComplete()
    }

    public fun onItemClick(article:Article){
        listener?.onItemClick(article)
    }

    interface NewsViewModelListener{
        fun onNewsDownloadComplete()
        fun onItemClick(article: Article)
    }

}