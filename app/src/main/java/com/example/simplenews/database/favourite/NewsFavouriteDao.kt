package com.example.simplenews.database.favourite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsFavouriteDao {
    @Query("SELECT * FROM databasenewsfavourite")
    fun getNews(): LiveData<List<DatabaseNewsFavourite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(news: DatabaseNewsFavourite)

    @Query("DELETE FROM databasenewsfavourite WHERE id = :newsId")
    fun delete(newsId: String)
}