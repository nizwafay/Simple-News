package com.example.simplenews.network.news

// This is to parse first level of our network result which look like
data class NewsContainer(
    val copyright: String,
    val response: Response,
    val status: String
)