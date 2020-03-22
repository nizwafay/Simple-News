package com.example.simplenews.database.request

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseNewsRequest(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "hits")
    val hits: Int
)

fun List<DatabaseNewsRequest>.asDomainModel(): Int {
    return if (this.isEmpty()) 0 else this[0].hits
}
