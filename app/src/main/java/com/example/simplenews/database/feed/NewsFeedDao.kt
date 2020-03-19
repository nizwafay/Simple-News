package com.example.simplenews.database.feed

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsFeedDao {
    @Query("SELECT * FROM databaseNewsFeed")
    fun getNews(): LiveData<List<DatabaseNewsFeed>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg news: DatabaseNewsFeed)
}