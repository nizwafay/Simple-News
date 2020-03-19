package com.example.simplenews.network.news

import com.squareup.moshi.Json

data class Multimedia(
    val caption: Any?,
    val credit: Any?,
    @Json(name = "crop_name")
    val cropName: String,
    val height: Int,
    val legacy: Legacy,
    val rank: Int,
    val subType: String,
    val subtype: String,
    val type: String,
    val url: String,
    val width: Int
)