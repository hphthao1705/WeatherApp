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
    var _liveData = liveData

    fun loadWeather(cityName:String):Boolean
    {
        viewModelScope.launch {
            repository.loadWeather(cityName)?.let {
                liveData.postValue(it)
            }
        }
        _liveData?.let {
            return true
        }
        return false
    }
}