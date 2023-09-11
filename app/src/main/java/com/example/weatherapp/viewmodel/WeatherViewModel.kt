package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){
    private val repository: WeatherRepository = WeatherRepository()
    private var liveData: MutableLiveData<Weather> = MutableLiveData()
    val _liveData = liveData
    private var liveDataAPI: MutableLiveData<String>? = null
    val _liveDataAPI = liveDataAPI
    fun loadWeather(cityName:String)
    {
        viewModelScope.launch {
            repository.loadWeather(cityName)?.let {
                liveData.postValue(it)
            }
            repository.error400(cityName).collect {
                liveDataAPI?.postValue("error")
            }
        }
    }
}