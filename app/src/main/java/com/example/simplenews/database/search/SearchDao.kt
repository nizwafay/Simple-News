package com.example.simplenews.database.search

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SearchDao {
    @Query("SELECT * FROM databasesearch")
    fun getSearchHistory(): LiveData<List<DatabaseSearch>>

    @Insert
    fun insert(keyword: DatabaseSearch)

    @Query("DELETE FROM databasesearch WHERE id = (SELECT id FROM databasesearch ORDER BY id ASC LIMIT 1)")
    fun deleteOldestItem()
}