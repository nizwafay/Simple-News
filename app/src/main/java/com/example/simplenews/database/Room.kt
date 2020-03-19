package com.example.simplenews.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {
    @Query("select * from databaseNews")
    fun getNews(): List<DatabaseNews>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg news: DatabaseNews)
}