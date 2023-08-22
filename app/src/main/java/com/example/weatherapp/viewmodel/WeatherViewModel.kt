package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){
    private val repository: WeatherRepository = WeatherRepository()
    private var list:ArrayList<Weather> = ArrayList()
    private var liveData: MutableLiveData<ArrayList<Weather>> = MutableLiveData()
    var _liveData = liveData

    fun loadWeather()
    {
        viewModelScope.launch {
            try {
                var flow = repository.loadWeather()
                flow.collect(){
                    it?.let {
                        list.add(it)
                    }
                }
                liveData.postValue(list)
            }catch (ex:Exception)
            {
                return@launch error(ex.message ?: ex.toString())
            }
        }
    }
}