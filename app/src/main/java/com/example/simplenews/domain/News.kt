package com.example.simplenews.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class News(
    val id: String,
    val title: String?,
    val snippet: String?,
    val imageUrl: String?,
    val date: String?,
    val webUrl: String?
) : Parcelable