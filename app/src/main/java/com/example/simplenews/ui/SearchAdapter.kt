package com.example.simplenews.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenews.R
import com.example.simplenews.databinding.SearchItemBinding

class SearchAdapter(private val clickListener: SearchListener)
    : ListAdapter<String, SearchViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val searchItemBinding: SearchItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            SearchViewHolder.LAYOUT,
            parent,
            false)

        return SearchViewHolder(searchItemBinding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
         holder.viewDataBinding.also {
             it.keyword = getItem(position)
             it.searchListener = clickListener
             it.executePendingBindings()
        }
    }
}

class SearchViewHolder(val viewDataBinding: SearchItemBinding)
    : RecyclerView.ViewHolder(viewDataBinding.root) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_item
    }
}

class SearchDiffCallback: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

class SearchListener(val clickItemListener: (String) -> Unit, val removeListener: (String) -> Unit) {
    fun onClick(keyword: String) = clickItemListener(keyword)
    fun onRemove(keyword: String) = removeListener(keyword)
}

