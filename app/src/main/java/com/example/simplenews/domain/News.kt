package com.example.simplenews.domain

data class News(
    val id: String,
    val title: String?,
    val snippet: String?,
    val imageUrl: String?,
    val date: String?,
    val webUrl: String?
)