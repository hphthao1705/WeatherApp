package com.example.weatherapp.view.city

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.repository.SearchRepository
import com.example.weatherapp.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(app: Application) : ViewModel() {
    private val repository: SearchRepository = SearchRepository(app)
    var errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorTitle_Favorite: MutableLiveData<String> = MutableLiveData("OOPS")
    val errorContent_Favorite: MutableLiveData<String> = MutableLiveData("No Data Found")
    val errorTitle_Search: MutableLiveData<String> = MutableLiveData("NOT FOUND")
    val errorContent_Search : MutableLiveData<String> = MutableLiveData("Please search another city!")
    var isSearch: MutableLiveData<Boolean> = MutableLiveData()
    var favoriteCities: MutableLiveData<ArrayList<Search>> = MutableLiveData()

    init {
//        checkListCityData()
        isSearch.value = false
        getFavoriteCities()
    }

    fun checkListCityData() {
        if (AppUtils.checkListCity()) {
            errorVisibility.postValue(View.GONE)
        } else {
            errorVisibility.postValue(View.VISIBLE)
        }
    }

    private fun getAll(): List<Search> = repository.getAllCity()
    fun getFavoriteCities() {
        favoriteCities.value?.let {
            favoriteCities.value!!.clear()
            favoriteCities.value!!.addAll(getAll())
        }
    }

    fun addNewCity(search: Search) {
        insertCity(search)
        Log.i("Check", "1")
    }

    fun insertCity(item: Search) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCity(item)
        Log.i("Check", "2")
    }

    fun deleteCity(city: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCity(city)
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