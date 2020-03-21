package com.example.simplenews.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenews.R
import com.example.simplenews.databinding.FragmentNewsFeedBinding
import com.example.simplenews.viewmodels.NewsViewModel

class NewsFeedFragment: Fragment() {
    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: NewsViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, NewsViewModel.Factory(activity.application))
            .get(NewsViewModel::class.java)
    }

    private var viewModelAdapter = NewsAdapter(NewsListener {
        Toast.makeText(context, it.webUrl, Toast.LENGTH_LONG).show()
    })

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.news.observe(viewLifecycleOwner, Observer {
            viewModel.onNewsRepoUpdated(it)
        })
        viewModel.newsForAdapter.observe(viewLifecycleOwner, Observer {
            viewModelAdapter.submitList(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentNewsFeedBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_feed, container, false)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.root.findViewById<RecyclerView>(R.id.newsFeedRV).apply {
            val linearLayoutManager = LinearLayoutManager(context)

            layoutManager = linearLayoutManager

            adapter = viewModelAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val countItem = linearLayoutManager.itemCount
                    val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                    viewModel.trackScroll(viewModelAdapter, countItem, lastVisiblePosition)
                }
            })
        }

        return binding.root
    }
}
