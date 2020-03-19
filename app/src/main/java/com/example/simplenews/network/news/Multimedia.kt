package com.example.simplenews.network.news

import com.squareup.moshi.Json

data class Multimedia(
    val caption: String?,
    val credit: String?,
    @Json(name = "crop_name")
    val cropName: String?,
    val height: Int?,
    val legacy: Legacy?,
    val rank: Int?,
    val subType: String?,
    val type: String?,
    val url: String?,
    val width: Int?
)