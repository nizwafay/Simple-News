package com.example.simplenews.viewmodels

import androidx.lifecycle.*

class NewsDetailViewModel: ViewModel() {
    private var _selectedNewsIndex = MutableLiveData<Int>()
    val selectedNewsIndex: LiveData<Int>
        get() = _selectedNewsIndex

    fun updateSelectedIndex(index: Int) {
        _selectedNewsIndex.value = index
    }

    fun onClickPreviousButton() {
        _selectedNewsIndex.value = _selectedNewsIndex.value?.minus(1)
    }

    fun onClickNextButton() {
        _selectedNewsIndex.value = _selectedNewsIndex.value?.plus(1)
    }
}