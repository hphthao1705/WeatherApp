package com.example.weatherapp.view.city

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.local.Search
import com.example.weatherapp.repository.SearchRepository
import com.example.weatherapp.view.city.adapter.CityAdapter
import com.example.weatherapp.view.city.adapter.SearchAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CityViewModel(app: Application) : ViewModel() {
    private val repository: SearchRepository = SearchRepository(app)
    var errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val searchVisibility: MutableLiveData<Int> = MutableLiveData()
    val favoriteVisibility: MutableLiveData<Int> = MutableLiveData()
    val emptyTitleFavorite: MutableLiveData<String> = MutableLiveData()
    val emptyContentFavorite: MutableLiveData<String> = MutableLiveData()
    val emptyTitleSearch: MutableLiveData<String> = MutableLiveData()
    val emptyContentSearch: MutableLiveData<String> = MutableLiveData()
    val errorTitle: MutableLiveData<String> = MutableLiveData()
    val errorContent: MutableLiveData<String> = MutableLiveData()
    var isSearch: MutableLiveData<Boolean> = MutableLiveData()
    var favoriteCities: List<Search> = listOf()
    val isDone: MutableLiveData<Boolean> = MutableLiveData()
    val isDoneDelete: MutableLiveData<Boolean> = MutableLiveData()
    var favoriteAdapter: CityAdapter = CityAdapter()
    var searchAdapter: SearchAdapter = SearchAdapter()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        searchVisibility.value = View.GONE
        errorVisibility.value = View.GONE
        favoriteVisibility.value = View.GONE
//        checkListCityData()
        isSearch.value = false
        emptyTitleFavorite.value = "OOPS"
        emptyContentFavorite.value = "No Data Found"
        emptyTitleSearch.value = "NOT FOUND"
        emptyContentSearch.value = "Please search another city!"
        loadingVisibility.value = View.VISIBLE
    }

//    fun checkListCityData() {
//        if (AppUtils.checkListCity()) {
//            errorVisibility.postValue(View.GONE)
//        } else {
//            errorVisibility.postValue(View.VISIBLE)
//        }
//    }

    fun getAll() {
        coroutineScope.launch {
            val result = async {
                favoriteCities = repository.getAllCity()
                return@async true
            }
            if(result.await()) {
                isDone.postValue(true)
            }
        }
    }

    fun addNewCity(item: Search) = coroutineScope.launch {
        repository.insertCity(item)
    }

    private fun deleteCity(city: String) = coroutineScope.launch {
        repository.deleteCity(city)
    }

    fun checkExistence(search: Search) {
        coroutineScope.launch {
            search?.let {
                val result = repository.checkExistence(search.name.orEmpty())
                if (result != 0) {
                    deleteCity(search.name.orEmpty())
                }
            }
        }
        isDoneDelete.value = true
    }

    class ViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CityViewModel(app) as T
            }
            throw IllegalArgumentException("Unable construct viewmodel")
        }
    }
//    fun showError(result: Int) {
//        errorVisibility.value = result
//        if (result == View.GONE) {
//            searchVisibility.value = View.VISIBLE
//        } else {
//            searchVisibility.value = View.GONE
//        }
//    }
}