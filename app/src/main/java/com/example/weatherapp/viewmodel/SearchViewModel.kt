package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.repository.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(app: Application):ViewModel() {
    private val repository: SearchRepository = SearchRepository(app)
    fun addNewCity(search: Search) {
        insertCity(search)
    }
    fun insertCity(item: Search) = viewModelScope.launch {
        repository.insertCity(item)
    }
    fun getAllNote(): LiveData<List<Search>> = repository.getAllCity()
    class ViewModelFactory(private val application: Application): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java))
            {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construct viewmodel")
        }
    }
}