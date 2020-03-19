package com.example.simplenews.network.news

import com.squareup.moshi.Json

data class Doc(
    val abstract: String?,
    val byline: Byline,
    @Json(name = "document_type")
    val documentType: String?,
    val headline: Headline?,
    @Json(name = "_id")
    val id: String,
    val keywords: List<Keyword?>,
    @Json(name = "lead_paragraph")
    val leadParagraph: String?,
    val multimedia: List<Multimedia?>,
    @Json(name = "news_desk")
    val newsDesk: String?,
    @Json(name = "pub_date")
    val pubDate: String?,
    @Json(name = "section_name")
    val sectionName: String?,
    val snippet: String?,
    val source: String?,
    @Json(name = "subsection_name")
    val subsectionName: String?,
    @Json(name = "type_of_material")
    val typeOfMaterial: String?,
    val uri: String?,
    @Json(name = "web_url")
    val webUrl: String?,
    @Json(name = "word_count")
    val wordCount: Int?
)