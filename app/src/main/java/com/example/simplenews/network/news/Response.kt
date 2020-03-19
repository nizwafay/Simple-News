package com.example.simplenews.network.news

data class Response(
    val docs: List<Doc>,
    val meta: Meta
)