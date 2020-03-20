package com.example.simplenews

import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.example.simplenews.viewmodels.NewsViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        viewModel = ViewModelProvider(this, NewsViewModel.Factory(this.application))
            .get(NewsViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)

        val searchItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnSearchClickListener { v ->
            val lp = v?.layoutParams
            lp?.width = ActionBar.LayoutParams.MATCH_PARENT
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.fetchNews(query, true)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }
}