package com.example.simplenews.database.favourite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseNewsFavourite constructor(
    @PrimaryKey
    val id: String,
    val title: String,
    val snippet: String,
    val imageUrl: String,
    val date: String,
    val webUrl: String
)