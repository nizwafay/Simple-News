package com.example.simplenews.domain

import android.os.Parcelable
import com.example.simplenews.database.favourite.DatabaseNewsFavourite
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    val id: String,
    val title: String?,
    val snippet: String?,
    val imageUrl: String?,
    val date: String?,
    val webUrl: String?
) : Parcelable

// convert domain model to database objects
fun News.asDatabaseModel(): DatabaseNewsFavourite {
    return DatabaseNewsFavourite(
        id = id,
        title = title,
        snippet = snippet,
        imageUrl = imageUrl,
        date = date,
        webUrl = webUrl
    )
}