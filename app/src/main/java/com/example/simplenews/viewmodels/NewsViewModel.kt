package com.example.simplenews.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplenews.database.getDatabase
import com.example.simplenews.repository.NewsRepository
import kotlinx.coroutines.*

class NewsViewModel(application: Application): AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val newsRepository = NewsRepository(database)

    init {
        fetchNews(clearDbFirst = true)
    }

    val news = newsRepository.news

    fun fetchNews(keyword: String? = null, clearDbFirst: Boolean = false) {
        viewModelJob.cancelChildren()
        viewModelScope.launch {
            newsRepository.getNews(keyword, clearDbFirst)
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