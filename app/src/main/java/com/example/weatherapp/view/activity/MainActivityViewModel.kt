package com.example.weatherapp.view.activity

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.usecase.CityUseCase
import com.example.weatherapp.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(private val cityUseCase: CityUseCase) : ViewModel() {
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val doneAPI: MutableLiveData<Boolean> = MutableLiveData()
    var errorVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
//        loadCities()
        loadCities2()
    }

    private fun loadCities() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                cityUseCase.getCityUIViewModel().collect {
                    it.onRight {
                        withContext(Dispatchers.Main) {
                            val response = async { AppUtils.saveListCity(it) }
                            response.await()?.let {
                                doneAPI.postValue(true)
                            }
                        }
                        errorVisibility.postValue(View.GONE)
                    }
                    it.onLeft {
                        errorVisibility.postValue(View.VISIBLE)
                    }
                }
                loadingVisibility.postValue(View.GONE)
            }
        } catch (ex: Exception) {
            errorVisibility.postValue(View.VISIBLE)
        }
    }

    private fun loadCities2() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                cityUseCase.getCityUIViewModel2().collect {
                    it.onRight {
                        withContext(Dispatchers.Main) {
                            val response = async { AppUtils.saveListCity(it) }
                            response.await()?.let {
                                doneAPI.postValue(true)
                            }
                        }
                        errorVisibility.postValue(View.GONE)
                    }
                    it.onLeft {
                        errorVisibility.postValue(View.VISIBLE)
                    }
                }
                loadingVisibility.postValue(View.GONE)
            }
        } catch (ex: Exception) {
            errorVisibility.postValue(View.VISIBLE)
        }
    }
}