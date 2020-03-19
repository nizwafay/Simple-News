package com.example.simplenews.database

import android.content.Context
import androidx.room.*
import com.example.simplenews.database.favourite.DatabaseNewsFavourite
import com.example.simplenews.database.favourite.NewsFavouriteDao
import com.example.simplenews.database.feed.DatabaseNewsFeed
import com.example.simplenews.database.feed.NewsFeedDao
import com.example.simplenews.database.search.DatabaseSearch
import com.example.simplenews.database.search.SearchDao

@Database(entities = [
    DatabaseNewsFeed::class,
    DatabaseNewsFavourite::class,
    DatabaseSearch::class], version = 1)
abstract class Databases: RoomDatabase() {
    abstract val newsFeedDao: NewsFeedDao
    abstract val newsFavouriteDao: NewsFavouriteDao
    abstract val searchDao: SearchDao

    companion object {
        @Volatile
        private var INSTANCE: Databases? = null

        fun getInstance(context: Context): Databases {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Databases::class.java,
                        "news_databases"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}