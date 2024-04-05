package com.example.weatherapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel(){
    //private val repository: WeatherRepository = WeatherRepository()
    private var liveData: MutableLiveData<Weather> = MutableLiveData()
    val errorVisibility:MutableLiveData<Int> = MutableLiveData()
    val weatherVisibility: MutableLiveData<Int> = MutableLiveData()
    val _liveData = liveData
    var error:String = ""
    suspend fun loadWeather(cityName:String)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loadWeather(cityName)?.let {
                error = ""
                liveData.postValue(it as Weather?)
                weatherVisibility.value = View.VISIBLE
                errorVisibility.value = View.GONE
            }
        }
        repository.error400(cityName).collect{
            errorVisibility.value = View.VISIBLE
            weatherVisibility.value = View.GONE
        }
    }
}