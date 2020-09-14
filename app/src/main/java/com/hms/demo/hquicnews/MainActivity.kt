package com.hms.demo.hquicnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,NewsAdapter.NewsViewHolder.onNewsClickListener, NewsClient.NewsClientListener {
    lateinit var news:ArrayList<Article>
    private val TAG = "HQUICNews"
    private val API_KEY="YOUR_API_KEY" //get your API key here: https://newsapi.org/register
    private val URL = "https://newsapi.org/v2/top-headlines?apiKey=$API_KEY"
    private val METHOD = "GET"
    private var adapter:NewsAdapter?=null
    private var loadingDialog:AlertDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeRefreshLayout.setOnRefreshListener(this)
        news=ArrayList()
        adapter= NewsAdapter(news,this)
        recyclerNews.layoutManager=LinearLayoutManager(this)
        recyclerNews.adapter=adapter
        loadingDialog=LoadingDialog.createDialog(this)
        loadingDialog?.show()
        getNews()



    }

    private fun getNews() {
        val country=Locale.getDefault().country
        val url= "$URL&country=$country"
        Log.e("URL",url)
        val downloader=NewsClient(this)
        downloader.apply {
            listener=this@MainActivity
            getNews(url,METHOD)
        }
    }

    override fun onRefresh() {
        getNews()
    }

    override fun onClickedArticle(position: Int) {
        val intent= Intent(Intent.ACTION_VIEW)
        intent.data= Uri.parse(news[position].url)
        startActivity(intent)
    }

    override fun onSuccess(news: ArrayList<Article>) {
        this.news.apply {
            clear()
            addAll(news)
        }
        runOnUiThread{
            swipeRefreshLayout.isRefreshing=false
            loadingDialog?.dismiss()
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onFailure(error: String) {
        AlertDialog.Builder(this).apply {
            setTitle(R.string.error_title)
            val message="${getString(R.string.error_message)} \n $error"
            setMessage(message)
            setPositiveButton(R.string.ok){ dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }
    }
}