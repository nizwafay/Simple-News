package com.example.simplenews.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.simplenews.database.getDatabase
import com.example.simplenews.domain.News
import com.example.simplenews.repository.NewsRepository
import kotlinx.coroutines.*

class NewsViewModel(application: Application): ViewModel() {
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
            newsRepository.getNews(keyword, clearDbFirst = initialFetch)
            _showTopLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val application: Application): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                val key = "UserProfileViewModel"
                return if(hashMapViewModel.containsKey(key)){
                    getViewModel(key) as T
                } else {
                    addViewModel(key, NewsViewModel(application))
                    getViewModel(key) as T
                }
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

        companion object {
            val hashMapViewModel = HashMap<String, ViewModel>()
            fun addViewModel(key: String, viewModel: ViewModel){
                hashMapViewModel[key] = viewModel
            }
            fun getViewModel(key: String): ViewModel? {
                return hashMapViewModel[key]
            }
        }
    }
}