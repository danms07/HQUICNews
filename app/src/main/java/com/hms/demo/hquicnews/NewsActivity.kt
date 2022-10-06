package com.hms.demo.hquicnews

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hms.demo.hquicnews.databinding.ActivityNewsBinding
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import java.util.*

class NewsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, NewsViewModel.NewsViewModelListener{

    //private var adapter:NewsAdapter?=null
    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        binding.swipeRefreshLayout.setOnRefreshListener(this)
        binding.recyclerNews.layoutManager=LinearLayoutManager(this)
        val adapter= NewsAdapter(viewModel)

        binding.recyclerNews.adapter=adapter
        viewModel.news.observe(this){
            adapter.news=it
            adapter.notifyDataSetChanged()

        }
        binding.swipeRefreshLayout.isRefreshing=true
        viewModel.listener=this
        viewModel.loadNews(this)
        showAd()
    }

    private fun showAd() {
        binding.hwBannerView.loadAd(AdParam.Builder().build())
        binding.hwBannerView.adListener=object:AdListener(){
            override fun onAdLoaded() {
                // Called when an ad is loaded successfully.
                showToast("Ad loaded.")
            }

            override fun onAdFailed(errorCode: Int) {
                // Called when an ad fails to be loaded.
                showToast(
                    String.format(
                        Locale.ROOT,
                        "Ad failed to load with error code %d.",
                        errorCode
                    )
                )
            }

            override fun onAdOpened() {
                // Called when an ad is opened.
                showToast(String.format("Ad opened "))
            }

            override fun onAdClicked() {
                // Called when a user taps an ad.
                showToast("Ad clicked")
            }

            override fun onAdLeave() {
                // Called when a user has left the app.
                showToast("Ad Leave")
            }

            override fun onAdClosed() {
                // Called when an ad is closed.
                showToast("Ad closed")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@NewsActivity, message, Toast.LENGTH_SHORT).show()
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