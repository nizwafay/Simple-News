package com.example.simplenews.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenews.R
import com.example.simplenews.databinding.NewsCardItemBinding
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
        val binding: NewsCardItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            NewsViewHolder.LAYOUT,
            parent,
            false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.viewDataBinding.news = newsData[position]
    }

    override fun getItemCount() = newsData.size
}

/**
 * ViewHolder for DevByte items. All work is done by data binding.
 */
class NewsViewHolder(val viewDataBinding: NewsCardItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.news_card_item
    }
}