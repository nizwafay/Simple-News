package com.example.simplenews.database.feed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplenews.domain.News

@Entity
data class DatabaseNewsFeed constructor(
    @PrimaryKey
    val id: String,
    val title: String?,
    val snippet: String?,
    val imageUrl: String?,
    val date: String?,
    val webUrl: String?
)

fun List<DatabaseNewsFeed>.asDomainModel(): List<News> {
    return map {
        News(
            title = it.title,
            snippet = it.snippet,
            imageUrl = it.imageUrl?.let { url ->
                val completeUrl = "https://static01.nyt.com/${url}"
                completeUrl
            },
            date = it.date,
            webUrl = it.webUrl
        )
    }
}