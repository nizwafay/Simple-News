package com.example.simplenews.network.news

import com.squareup.moshi.Json

data class Headline(
    @Json(name = "content_kicker")
    val contentKicker: String?,
    val kicker: String?,
    val main: String?,
    val name: String?,
    @Json(name = "print_headline")
    val printHeadline: String?,
    val seo: String?,
    val sub: String?
)