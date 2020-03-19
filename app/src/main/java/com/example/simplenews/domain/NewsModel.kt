package com.example.simplenews.domain

data class NewsModel(
    val title: String,
    val snippet: String,
    val imageUrl: String,
    val date: String,
    val webUrl: String
)