package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Error
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){
    private val repository: WeatherRepository = WeatherRepository()
    private var liveData: MutableLiveData<Weather> = MutableLiveData()
    val _liveData = liveData
    var errorMessage:String = ""
    suspend fun loadWeather(cityName:String):String
    {
        viewModelScope.launch {
            repository.loadWeather(cityName)?.let {
                liveData.postValue(it)
            }
        }
        repository.error400(cityName).collect {
            errorMessage = it
        }
        return errorMessage
    }
}