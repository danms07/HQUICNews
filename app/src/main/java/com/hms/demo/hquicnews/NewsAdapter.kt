package com.hms.demo.hquicnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hms.demo.hquicnews.databinding.ArticleBinding
import kotlinx.android.synthetic.main.item_view.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(private val news:ArrayList<Article>,private val newsVM:NewsViewModel): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(private val itemBinding: ArticleBinding) : RecyclerView.ViewHolder(itemBinding.root){
        public fun bind(article: Article, newsVM: NewsViewModel){
            itemBinding.article=article
            itemBinding.mainVM=newsVM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        val articleBinding=ArticleBinding.inflate(layoutInflater,parent,false)
        return NewsViewHolder(articleBinding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news[position],newsVM)
    }

    override fun getItemCount(): Int {
        return news.size
    }
}