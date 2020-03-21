package com.example.simplenews.viewmodels

import androidx.lifecycle.*

class NewsDetailViewModel: ViewModel() {
    private var _selectedNewsIndex = MutableLiveData<Int>()
    val selectedNewsIndex: LiveData<Int>
        get() = _selectedNewsIndex

    fun updateSelectedIndex(index: Int) {
        _selectedNewsIndex.value = index
    }
}