package com.example.simplenews.network

import com.example.simplenews.domain.NewsModel
import com.example.simplenews.network.news.Response

// conver network result to database objects
fun Response.asDomainModel(): List<NewsModel> {
    return docs.map {
        NewsModel(
            title = it.headline.main,
            snippet = it.snippet,
            imageUrl = it.multimedia[0].url,
            date = it.pubDate,
            webUrl = it.webUrl
        )
    }
}