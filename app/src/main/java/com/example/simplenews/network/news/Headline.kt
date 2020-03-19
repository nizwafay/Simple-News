package com.example.simplenews.network.news

import com.squareup.moshi.Json

data class Headline(
    @Json(name = "content_kicker")
    val contentKicker: Any?,
    val kicker: Any?,
    val main: String,
    val name: Any?,
    @Json(name = "print_headline")
    val printHeadline: Any?,
    val seo: Any?,
    val sub: Any?
)