package com.example.simplenews.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.simplenews.R
import com.example.simplenews.databinding.FragmentNewsDetailBinding
import com.example.simplenews.viewmodels.NewsDetailViewModel
import kotlinx.android.synthetic.main.fragment_news_detail.*

class NewsDetailFragment: Fragment() {
    private lateinit var viewModel: NewsDetailViewModel

    private val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentNewsDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_detail, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this).get(NewsDetailViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.selectedNewsIndex.observe(viewLifecycleOwner, Observer {
            args.news?.let { news ->
                detailWebView.loadUrl(news[it].webUrl)
            }

            if (it > 0) previousButton?.visibility = View.VISIBLE
            else previousButton?.visibility = View.GONE
            args.news?.let { news ->
                if (it < news.size - 1) nextButton?.visibility = View.VISIBLE
                else nextButton?.visibility = View.GONE
            }
        })

        viewModel.updateSelectedIndex(args.index)

        previousButton?.setOnClickListener { viewModel.onClickPreviousButton() }
        nextButton?.setOnClickListener { viewModel.onClickNextButton() }
    }
}