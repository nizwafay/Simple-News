package com.example.simplenews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenews.database.getDatabase
import com.example.simplenews.repository.NewsRepository
import com.example.simplenews.ui.NewsAdapter
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NewsAdapter(arrayOf(""))

        recyclerView = findViewById<RecyclerView>(R.id.news_list).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        val scope = CoroutineScope(job + Dispatchers.IO)
        val database = getDatabase(applicationContext)
        val newsRepository = NewsRepository(database)

        scope.launch {
            newsRepository.getNews("jokowi")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
