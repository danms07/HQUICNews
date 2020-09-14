package com.hms.demo.hquicnews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(val news:ArrayList<Article>,var listener: NewsViewHolder.onNewsClickListener?): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View, var listener:onNewsClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        public fun init(article: Article){
            itemView.title.text=article.title
            itemView.content.text=article.description
            val date= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).parse(article.time)
            itemView.time.text=date?.toString()
            itemView.setOnClickListener(this)
        }

        interface onNewsClickListener{
            fun onClickedArticle(position: Int)
        }

        override fun onClick(v: View?) {
            listener?.onClickedArticle(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view,parent,false)
        return NewsViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.init(news[position])
    }

    override fun getItemCount(): Int {
        return news.size
    }
}