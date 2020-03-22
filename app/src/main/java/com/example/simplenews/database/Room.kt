package com.example.simplenews.database

import android.content.Context
import androidx.room.*
import com.example.simplenews.database.favourite.DatabaseNewsFavourite
import com.example.simplenews.database.favourite.NewsFavouriteDao
import com.example.simplenews.database.feed.DatabaseNewsFeed
import com.example.simplenews.database.feed.NewsFeedDao
import com.example.simplenews.database.request.DatabaseNewsRequest
import com.example.simplenews.database.request.NewsRequestDao
import com.example.simplenews.database.search.DatabaseSearch
import com.example.simplenews.database.search.SearchDao

@Database(entities = [
    DatabaseNewsFeed::class,
    DatabaseNewsFavourite::class,
    DatabaseSearch::class,
    DatabaseNewsRequest::class], version = 4,
    exportSchema = false)
abstract class NewsDatabases: RoomDatabase() {
    abstract val newsFeedDao: NewsFeedDao
    abstract val newsFavouriteDao: NewsFavouriteDao
    abstract val searchDao: SearchDao
    abstract val newsRequestDao: NewsRequestDao
}

private lateinit var INSTANCE: NewsDatabases

fun getDatabase(context: Context): NewsDatabases {
    synchronized(NewsDatabases::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                NewsDatabases::class.java,
                "news_databases")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}