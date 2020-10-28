package com.hms.demo.hquicnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hms.demo.hquicnews.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, NewsViewModel.NewsViewModelListener{

    private var adapter:NewsAdapter?=null
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.recyclerNews.layoutManager=LinearLayoutManager(this)

        viewModel.news.observe(this){
            adapter= NewsAdapter(it,viewModel)
            binding.recyclerNews.adapter=adapter
        }
        binding.swipeRefreshLayout.isRefreshing=true
        viewModel.listener=this
        viewModel.loadNews(this)
    }

    override fun onRefresh() {
        viewModel.getNews(this)
    }

    override fun onNewsDownloadComplete() {
        binding.swipeRefreshLayout.isRefreshing=false
    }

    override fun onItemClick(article: Article) {
        val intent= Intent(Intent.ACTION_VIEW)
        intent.data= Uri.parse(article.url)
        startActivity(intent)
    }
}