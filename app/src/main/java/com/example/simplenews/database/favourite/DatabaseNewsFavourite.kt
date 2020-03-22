package com.example.simplenews.database.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.simplenews.domain.News

@Entity
data class DatabaseNewsFavourite constructor(
    @PrimaryKey
    val id: String,
    val title: String?,
    val snippet: String?,
    val imageUrl: String?,
    val date: String?,
    val webUrl: String?
)

fun List<DatabaseNewsFavourite>.asDomainModel(): List<News> {
    return map {
        News(
            id = it.id,
            title = it.title,
            snippet = it.snippet,
            imageUrl = it.imageUrl,
            date = it.date,
            webUrl = it.webUrl
        )
    }
}