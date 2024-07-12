package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.repository.CityRepository
import com.example.weatherapp.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: CityRepository) : ViewModel() {
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val doneAPI: MutableLiveData<Boolean> = MutableLiveData()
    var list: ArrayList<Data>? = arrayListOf()

    init {
        loadCities()
    }

    fun loadCities() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val flow = repository.loadCity()
                flow.collect() {
                    Log.d("Thao Ho", it.toString())
                    AppUtils.saveListCity(it)
                    list?.addAll(it)
                }
            } catch (e: Exception) {
            } finally {
                doneAPI.postValue(true)
            }
        }
    }
}