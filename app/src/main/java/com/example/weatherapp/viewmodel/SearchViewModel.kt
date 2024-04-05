package com.example.weatherapp.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(app: Application) : ViewModel() {
    private val repository: SearchRepository = SearchRepository(app)
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val searchVisibility: MutableLiveData<Int> = MutableLiveData()
    fun addNewCity(search: Search) {
        insertCity(search)
        Log.i("Check", "1")
    }

    fun insertCity(item: Search) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCity(item)
        Log.i("Check", "2")
    }

    fun getAllNote(): LiveData<List<Search>> = repository.getAllCity()
    class ViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(app) as T
            }
            throw IllegalArgumentException("Unable construct viewmodel")
        }
    }

    fun deleteCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCity(city)
    }

    fun showError(result: Int) {
        errorVisibility.value = result
        if (result == View.GONE) {
            searchVisibility.value = View.VISIBLE
        } else {
            searchVisibility.value = View.GONE
        }
    }
}