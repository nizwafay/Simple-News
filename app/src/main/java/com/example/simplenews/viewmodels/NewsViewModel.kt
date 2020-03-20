package com.example.simplenews.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.simplenews.database.getDatabase
import com.example.simplenews.domain.News
import com.example.simplenews.repository.NewsRepository
import kotlinx.coroutines.*

class NewsViewModel(application: Application): AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val newsRepository = NewsRepository(database)

    val news: LiveData<List<News>> = newsRepository.news

    private var _showTopLoading = MutableLiveData<Boolean>()
    val showTopLoading: LiveData<Boolean>
        get() = _showTopLoading

    init {
        fetchNews(initialFetch = true)
    }

    fun fetchNews(keyword: String? = null, initialFetch: Boolean = false) {
        viewModelJob.cancelChildren()
        _showTopLoading.value = initialFetch
        viewModelScope.launch {
            newsRepository.getNews(keyword, initialFetch)
            _showTopLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}