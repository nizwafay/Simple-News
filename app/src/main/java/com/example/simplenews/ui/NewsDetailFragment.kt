package com.example.simplenews.ui

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.simplenews.R
import com.example.simplenews.databinding.FragmentNewsDetailBinding
import com.example.simplenews.viewmodels.NewsDetailViewModel

class NewsDetailFragment: Fragment() {

    private val viewModel: NewsDetailViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, NewsDetailViewModel.Factory(activity.application))
            .get(NewsDetailViewModel::class.java)
    }

    val args: NewsDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentNewsDetailBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_detail, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }
}