package com.example.simplenews.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenews.R
import com.example.simplenews.domain.News

class NewsAdapter: RecyclerView.Adapter<NewsViewHolder>() {

    var newsData: List<News> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.text_item_view, parent, false) as TextView
        return NewsViewHolder(textView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.textView.text = newsData[position].title
    }

    override fun getItemCount() = newsData.size
}

class NewsViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)