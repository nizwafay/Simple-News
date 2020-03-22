package com.example.simplenews.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.simplenews.database.getDatabase
import com.example.simplenews.database.search.DatabaseSearch
import com.example.simplenews.domain.News
import com.example.simplenews.domain.asDatabaseModel
import com.example.simplenews.repository.NewsRepository
import com.example.simplenews.ui.NewsAdapter
import kotlinx.coroutines.*
import kotlin.math.ceil

class NewsFeedViewModel(application: Application): AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val newsRepository = NewsRepository(database)

    val news: LiveData<List<News>> = newsRepository.news
    private val keywordRepo: LiveData<String?> = newsRepository.keyword
    val latestHits: LiveData<Int> = newsRepository.hits

    private var _newsForAdapter = MutableLiveData<List<News?>>()
    val newsForAdapter: LiveData<List<News?>>
        get() = _newsForAdapter

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private var _showTopLoading = MutableLiveData<Boolean>()
    val showTopLoading: LiveData<Boolean>
        get() = _showTopLoading

    private val searchHistoryDatabase = database.searchDao.getSearchHistory()
    val searchHistory: LiveData<List<String>> = Transformations.map(searchHistoryDatabase) {
        it.map { databaseSearch ->
            databaseSearch.keyword
        }.reversed()
    }

    init {
        fetchNews(initialFetch = true)
    }

    private fun fetchNews(keyword: String? = null, page: Int? = null, initialFetch: Boolean = false) {
        viewModelJob.cancelChildren()
        if (initialFetch || isLoading.value != true) {
            _showTopLoading.value = initialFetch
            _isLoading.value = true
            viewModelScope.launch {
                newsRepository.getNews(keyword, page, initialFetch)
                _showTopLoading.value = false
                _isLoading.value = false
            }
        }
    }

    fun saveNews(news: News) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.newsFavouriteDao.insert(news.asDatabaseModel())
            }
        }
    }

    fun onSearchNews(keyword: String) {
        fetchNews(keyword, initialFetch = true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (searchHistory.value?.size == 10) {
                    database.searchDao.deleteOldestItem()
                }
                database.searchDao.insert(DatabaseSearch(keyword = keyword))
            }
        }
    }

    fun onDeleteSearch(keyword: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.searchDao.delete(keyword)
            }
        }
    }

    fun onNewsRepoUpdated(news: List<News>) {
        _newsForAdapter.value = news
    }

    fun trackScroll(newsAdapter: NewsAdapter?, countItem: Int, lastVisiblePosition: Int) {
        val isLastPosition = countItem.minus(1) == lastVisiblePosition
        if (countItem > 0 && isLastPosition && isLoading.value != true
            && countItem < latestHits.value ?: -1
        ) {
            val page = ceil(countItem/10.0).toInt()
            newsForAdapter.value?.let {
                if (it[it.size - 1] != null) {
                    _newsForAdapter.value = newsForAdapter.value?.plus(listOf(null))
                    newsAdapter?.notifyItemInserted(countItem)
                }
            }
            fetchNews(keywordRepo.value, page)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsFeedViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsFeedViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}