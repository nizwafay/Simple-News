package com.example.simplenews.network

import com.example.simplenews.database.DatabaseNews
import com.example.simplenews.domain.News
import com.example.simplenews.network.news.Response

// TODO: buat apa?
fun Response.asDomainModel(): List<News> {
    return docs.map {
        News(
            title = it.headline.main,
            snippet = it.snippet,
            imageUrl = it.multimedia[0].url,
            date = it.pubDate,
            webUrl = it.webUrl
        )
    }
}

// convert network result to database objects
fun Response.asDatabaseModel(): Array<DatabaseNews> {
    return docs.map {
        DatabaseNews(
            id = it.id,
            title = it.headline.main,
            snippet = it.snippet,
            imageUrl = it.multimedia[0].url,
            date = it.pubDate,
            webUrl = it.webUrl
        )
    }.toTypedArray()
}