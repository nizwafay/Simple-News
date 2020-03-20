package com.example.simplenews

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import com.example.simplenews.database.NewsDatabases
import com.example.simplenews.database.getDatabase
import com.example.simplenews.repository.NewsRepository
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database: NewsDatabases by lazy {
        val context = requireNotNull(this)
        getDatabase(context)
    }

    private lateinit var newsRepository: NewsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        newsRepository = NewsRepository(database)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnSearchClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val lp = v?.layoutParams
                lp?.width = ActionBar.LayoutParams.MATCH_PARENT
            }
        })

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModelJob.cancelChildren()
                viewModelScope.launch {
                    newsRepository.getNews(query, true)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelJob.cancel()
    }
}