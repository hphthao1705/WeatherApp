package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.dsl.module

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel(){
    //private val repository: WeatherRepository = WeatherRepository()
    private var liveData: MutableLiveData<Weather> = MutableLiveData()
    val _liveData = liveData
    var error:String = ""
    suspend fun loadWeather(cityName:String):String
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadWeather(cityName)?.let {
                error = ""
                liveData.postValue(it as Weather?)
            }
        }
        repository.error400(cityName).collect{
            error = it
        }
        return error
    }
}