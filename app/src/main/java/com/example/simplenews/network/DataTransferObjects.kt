package com.example.simplenews.network

import com.example.simplenews.database.feed.DatabaseNewsFeed
import com.example.simplenews.database.request.DatabaseNewsRequest
import com.example.simplenews.network.news.Meta
import com.example.simplenews.network.news.Response

// convert network result to database objects
fun Response.asDatabaseModel(): Array<DatabaseNewsFeed> {
    return docs.map {
        DatabaseNewsFeed(
            id = it.id,
            title = it.headline?.main,
            snippet = it.snippet,
            imageUrl = if (it.multimedia.isNullOrEmpty()) null else it.multimedia[0]?.url,
            date = it.pubDate,
            webUrl = it.webUrl
        )
    }.toTypedArray()
}

fun Meta.asDatabaseModel(): DatabaseNewsRequest {
    return DatabaseNewsRequest(id = 0, hits = hits)
}