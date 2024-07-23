package com.example.weatherapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.usecase.WeatherUseCase
import com.example.weatherapp.viewmodel.uiViewModel.WeatherUIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayWeatherViewModel(private val weatherUseCase: WeatherUseCase) : ViewModel() {
    val weather: MutableLiveData<WeatherUIViewModel> = MutableLiveData()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()

    //error
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorTitle: MutableLiveData<String> = MutableLiveData("NO RESULTS")
    val errorContent: MutableLiveData<String> =
        MutableLiveData("Sorry, there are no results for this search.\nPlease try another place")

    init {
        loadingVisibility.postValue(View.VISIBLE)
    }

    fun loadWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherUseCase.getWeather(city).collect {
                it.onRight {
                    withContext(Dispatchers.Main) {
                        weather.value = it
                    }
                    errorVisibility.postValue(View.GONE)
                }
                it.onLeft {
                    errorVisibility.postValue(View.VISIBLE)
                }
            }
        }
    }
}