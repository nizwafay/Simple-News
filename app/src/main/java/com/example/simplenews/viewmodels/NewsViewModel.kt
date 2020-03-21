package com.example.simplenews.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.simplenews.database.getDatabase
import com.example.simplenews.domain.News
import com.example.simplenews.network.news.Meta
import com.example.simplenews.repository.NewsRepository
import com.example.simplenews.ui.NewsAdapter
import kotlinx.coroutines.*
import kotlin.math.ceil

class NewsViewModel(application: Application): AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val newsRepository = NewsRepository(database)

    val news: LiveData<List<News>> = newsRepository.news
    private val keywordRepo: LiveData<String?> = newsRepository.keyword
    private val metaRepo: LiveData<Meta> = newsRepository.meta

    private var _newsForAdapter = MutableLiveData<List<News?>>()
    val newsForAdapter: LiveData<List<News?>>
        get() = _newsForAdapter

    private var isLoading = MutableLiveData<Boolean>()
    private var _showTopLoading = MutableLiveData<Boolean>()
    val showTopLoading: LiveData<Boolean>
        get() = _showTopLoading

    init {
        fetchNews(initialFetch = true)
    }

    fun fetchNews(keyword: String? = null, page: Int? = null, initialFetch: Boolean = false) {
        viewModelJob.cancelChildren()
        if (initialFetch || isLoading.value != true) {
            _showTopLoading.value = initialFetch
            isLoading.value = true
            viewModelScope.launch {
                newsRepository.getNews(keyword, page, initialFetch)
                _showTopLoading.value = false
                isLoading.value = false
            }
        }
    }

    fun onNewsRepoUpdated(news: List<News>) {
        _newsForAdapter.value = news
    }

    fun trackScroll(newsAdapter: NewsAdapter?, countItem: Int, lastVisiblePosition: Int) {
        val isLastPosition = countItem.minus(1) == lastVisiblePosition
        if (countItem > 0 && isLastPosition && isLoading.value != true
            && countItem < metaRepo.value?.hits ?: -1
        ) {
            val page = ceil(countItem/10.0).toInt()
            _newsForAdapter.value = newsForAdapter.value?.plus(listOf(null))
            newsAdapter?.notifyItemInserted(countItem)
            fetchNews(keywordRepo.value, page)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}