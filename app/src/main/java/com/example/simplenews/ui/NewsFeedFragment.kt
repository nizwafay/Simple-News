package com.example.simplenews.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenews.R
import com.example.simplenews.databinding.FragmentNewsFeedBinding
import com.example.simplenews.viewmodels.NewsFeedViewModel

class NewsFeedFragment: Fragment() {
    private lateinit var manager: GridLayoutManager
    private lateinit var menuItem: Menu
    private lateinit var binding: FragmentNewsFeedBinding

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: NewsFeedViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, NewsFeedViewModel.Factory(activity.application))
            .get(NewsFeedViewModel::class.java)
    }

    private var viewModelAdapter: NewsAdapter? = null
    private var searchAdapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_news_feed, container, false)

        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModelAdapter = NewsAdapter(
            NewsListener {
                view?.findNavController()?.navigate(
                    NewsFeedFragmentDirections.actionNewsFeedFragmentToNewsDetailFragment(
                        it, viewModel.news.value?.toTypedArray()))},
            NewsFavoriteListener { viewModel.saveNews(it) })

        binding.newsFeedRV.apply {
            activity?.let {
                manager = GridLayoutManager(it, if (
                    it.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                ) 4 else 1)
            }
            layoutManager = manager
            adapter = viewModelAdapter

            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val countItem = manager.itemCount
                    val lastVisiblePosition = manager.findLastVisibleItemPosition()

                    viewModel.trackScroll(viewModelAdapter, countItem, lastVisiblePosition)
                }
            })
        }

        searchAdapter = SearchAdapter(SearchListener({
            viewModel.onSearchNews(it)
            val searchItem = menuItem.findItem(R.id.app_bar_search)
            val searchView = searchItem.actionView as SearchView
            viewModel.onSearchNews(it)
            searchView.clearFocus()
            binding.searchRvWrapper.visibility = View.GONE
            searchItem.collapseActionView()
        }, { viewModel.onDeleteSearch(it) }))

        binding.searchRV.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchRvWrapper.setOnClickListener {
            val searchItem = menuItem.findItem(R.id.app_bar_search)
            val searchView = searchItem.actionView as SearchView
            searchView.clearFocus()
            binding.searchRvWrapper.visibility = View.GONE
            searchItem.collapseActionView()
        }
        binding.searchRV.itemAnimator = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.news.observe(viewLifecycleOwner, Observer {
            viewModel.onNewsRepoUpdated(it)
        })
        viewModel.newsForAdapter.observe(viewLifecycleOwner, Observer {
            viewModelAdapter?.submitList(it)
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            viewModel.newsForAdapter.value?.let { news ->
                if (isLoading == false && news.isNotEmpty() && news[news.size - 1] == null) {
                    viewModelAdapter?.submitList(viewModel.news.value)
                }
            }
        })
        viewModel.latestHits.observeForever {}
        viewModel.searchHistory.observe(viewLifecycleOwner, Observer {
            searchAdapter?.submitList(it)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuItem = menu
        inflater.inflate(R.menu.menu_item, menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                binding.searchRvWrapper.visibility = View.VISIBLE
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding.searchRvWrapper.visibility = View.GONE
                return true
            }
        })

        searchView.setOnSearchClickListener { v ->
            val lp = v?.layoutParams
            lp?.width = ActionBar.LayoutParams.MATCH_PARENT
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.onSearchNews(query)
                searchView.clearFocus()
                binding.searchRvWrapper.visibility = View.GONE
                searchItem.collapseActionView()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            view?.findNavController()?.navigate(
                NewsFeedFragmentDirections.actionNewsFeedFragmentToNewsFavoriteFragment()
            )
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}
