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
    var _liveData = liveData

    suspend fun loadWeather(cityName:String)
    {
        viewModelScope.launch {
            repository.loadWeather(cityName)?.let {
                if(it == null)
                {
                    var weather = Weather(Error("404","Error"),null,null)
                    liveData.postValue(weather)
                }
                else
                {
                    liveData.postValue(it)
                }
            }
        }
    }
}