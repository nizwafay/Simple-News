package com.example.simplenews.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenews.R
import com.example.simplenews.databinding.NewsCardItemBinding
import com.example.simplenews.domain.News

private const val VIEW_TYPE_LOADING = 0
private const val VIEW_TYPE_ITEM = 1

class NewsAdapter(private val clickListener: NewsListener):
    ListAdapter<News?, RecyclerView.ViewHolder>(NewsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val newsCardBinding: NewsCardItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            NewsViewHolder.LAYOUT,
            parent,
            false)

        val loadingView = LayoutInflater.from(parent.context)
            .inflate(LoadingViewHolder.LAYOUT, parent, false)

        return when (viewType) {
            VIEW_TYPE_LOADING -> LoadingViewHolder(loadingView)
            VIEW_TYPE_ITEM -> NewsViewHolder(newsCardBinding)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsViewHolder -> {
                holder.viewDataBinding.also {
                    it.position = position
                    it.news = getItem(position)
                    it.newsListener = clickListener
                    it.executePendingBindings()
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            null -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }
}

class NewsViewHolder(val viewDataBinding: NewsCardItemBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.news_card_item
    }
}
class LoadingViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.loading_item
    }
}

class NewsDiffCallback: DiffUtil.ItemCallback<News?>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }
}

class NewsListener(val clickListener: (Int) -> Unit) {
    fun onClick(index: Int) = clickListener(index)
}