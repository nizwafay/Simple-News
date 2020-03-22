package com.example.simplenews.database.request

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsRequestDao {
    @Query("SELECT * FROM databasenewsrequest")
    fun getHits(): LiveData<List<DatabaseNewsRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(hits: DatabaseNewsRequest)
}