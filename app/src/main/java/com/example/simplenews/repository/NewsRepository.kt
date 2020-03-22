package com.example.simplenews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.simplenews.database.NewsDatabases
import com.example.simplenews.database.feed.asDomainModel
import com.example.simplenews.database.request.asDomainModel
import com.example.simplenews.domain.News
import com.example.simplenews.network.Network
import com.example.simplenews.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class NewsRepository(private val newsDatabases: NewsDatabases) {
    val news: LiveData<List<News>> = Transformations.map(newsDatabases.newsFeedDao.getNews()) {
        it.asDomainModel()
    }

    private var _keyword = MutableLiveData<String?>()
    val keyword: LiveData<String?>
        get() = _keyword

    val hits: LiveData<Int> = Transformations.map(newsDatabases.newsRequestDao.getHits()) {
        it.asDomainModel()
    }

    suspend fun getNews(keyword: String? = null, page: Int? = null, clearDbFirst: Boolean = false) {
        _keyword.value = keyword
        withContext(Dispatchers.IO) {
            try {
                val news = Network.service.getNews(keyword, page)
                if (clearDbFirst) {
                    newsDatabases.newsFeedDao.deleteAll()
                }
                newsDatabases.newsFeedDao.insertAll(*news.response.asDatabaseModel())
                newsDatabases.newsRequestDao.insert(news.response.meta.asDatabaseModel())
            } catch (e: Exception) {}
        }
    }
}