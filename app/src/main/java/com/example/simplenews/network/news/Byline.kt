package com.example.simplenews.network.news

data class Byline(
    val organization: String?,
    val original: String?,
    val person: List<Person?>
)