package com.example.weatherapp.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.repository.CityRepository
import com.example.weatherapp.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(private val cityRepository: CityRepository) : ViewModel() {
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val doneAPI: MutableLiveData<Boolean> = MutableLiveData()
    var list: ArrayList<Data>? = arrayListOf()
    var errorVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        loadCities()
    }

    private fun loadCities() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                cityRepository.loadCity().collect {
                    it.onRight {
                        withContext(Dispatchers.Main) {
                            it?.data?.let { it1 ->
                                AppUtils.saveListCity(it1)
                                list?.addAll(it1)
                                Log.d("Thao Ho", "OK")
                            }
                        }
                    }
                    it.onLeft {
                        errorVisibility.postValue(View.VISIBLE)
                    }
                }
                loadingVisibility.postValue(View.GONE)
            }
        } catch (ex: Exception) {
            errorVisibility.postValue(View.VISIBLE)
        } finally {
            doneAPI.postValue(true)
        }
    }
}