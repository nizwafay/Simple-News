package com.example.simplenews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.simplenews.database.NewsDatabases
import com.example.simplenews.database.feed.asDomainModel
import com.example.simplenews.domain.News
import com.example.simplenews.network.Network
import com.example.simplenews.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(private val newsDatabases: NewsDatabases) {
    val news: LiveData<List<News>> = Transformations.map(newsDatabases.newsFeedDao.getNews()) {
        it.asDomainModel()
    }

    suspend fun getNews(keyword: String) {
        withContext(Dispatchers.IO) {
            val news = Network.service.getNews(keyword)
            newsDatabases.newsFeedDao.insertAll(*news.response.asDatabaseModel())
        }
    }
}