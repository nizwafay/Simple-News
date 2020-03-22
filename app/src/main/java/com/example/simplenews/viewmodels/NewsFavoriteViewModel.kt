package com.example.simplenews.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.simplenews.database.favourite.asDomainModel
import com.example.simplenews.database.getDatabase
import com.example.simplenews.domain.News
import kotlinx.coroutines.*

class NewsFavoriteViewModel(application: Application): AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)

    val news: LiveData<List<News>> = Transformations.map(database.newsFavouriteDao.getNews()) {
        it.asDomainModel()
    }

    fun deleteNews(news: News) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                database.newsFavouriteDao.delete(news.id)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsFavoriteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return NewsFavoriteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}