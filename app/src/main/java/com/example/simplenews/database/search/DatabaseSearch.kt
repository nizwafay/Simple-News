package com.example.simplenews.database.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseSearch(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "keyword")
    val keyword: String
)