package com.example.simplenews.domain

data class News(
    val title: String,
    val snippet: String,
    val imageUrl: String,
    val date: String,
    val webUrl: String
)