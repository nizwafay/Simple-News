package com.example.simplenews.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenews.R
import com.example.simplenews.databinding.FragmentNewsFavoriteBinding
import com.example.simplenews.viewmodels.NewsFavoriteViewModel

class NewsFavoriteFragment: Fragment() {
    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: NewsFavoriteViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, NewsFavoriteViewModel.Factory(activity.application))
            .get(NewsFavoriteViewModel::class.java)
    }

     private var viewModelAdapter: NewsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentNewsFavoriteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_favorite, container, false)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModelAdapter = NewsAdapter(
            NewsListener {
                view?.findNavController()?.navigate(
                    NewsFavoriteFragmentDirections.actionNewsFavoriteFragmentToNewsDetailFragment(
                        it, viewModel.news.value?.toTypedArray()))},
            NewsFavoriteListener { viewModel.deleteNews(it) },
            isFavoriteFragment = true)

        binding.root.findViewById<RecyclerView>(R.id.newsFavoriteRV).apply {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = viewModelAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.news.observe(viewLifecycleOwner, Observer {
            viewModelAdapter?.submitList(it)
        })
    }
}