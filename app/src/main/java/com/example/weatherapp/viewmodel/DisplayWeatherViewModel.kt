package com.example.weatherapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherProperties
import com.example.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisplayWeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    val cityName: MutableLiveData<String> = MutableLiveData()
    val condition: MutableLiveData<String> = MutableLiveData()
    val tempurature: MutableLiveData<String> = MutableLiveData()
    val weatherProperties: MutableLiveData<ArrayList<WeatherProperties>> =
        MutableLiveData(arrayListOf())
    val imageWeather: MutableLiveData<String> = MutableLiveData()

    //error
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorTitle: MutableLiveData<String>? = MutableLiveData("NO RESULTS")
    val errorContent: MutableLiveData<String>? =
        MutableLiveData("Sorry, there are no results for this search.\nPlease try another place")

    init {
        errorVisibility.postValue(View.GONE)
    }

    fun loadWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.loadWeather(city)

            response?.let {
                imageWeather.postValue(it.current?.condition?.icon)
                cityName.postValue(it.location?.name)
                condition.postValue(it.current?.condition?.text)
                tempurature.postValue(it.current?.temp_c)
                weatherProperties.postValue(
                    loadWeatherProperties(
                        feelslike = it.current?.feelslike_c.toString().orEmpty(),
                        wind = it.current?.wind_dir.orEmpty(),
                        humidity = it.current?.humidity.toString()
                    )
                )
            } ?: run {
                errorVisibility.postValue(View.VISIBLE)
            }
        }
    }

    private fun loadWeatherProperties(
        feelslike: String,
        wind: String,
        humidity: String
    ): ArrayList<WeatherProperties> {
        val weatherlist: ArrayList<WeatherProperties> = arrayListOf()
        weatherlist.add(WeatherProperties(R.drawable.umbrella, feelslike, "Rain"))
        weatherlist.add(WeatherProperties(R.drawable.wind, wind, "Wind"))
        weatherlist.add(WeatherProperties(R.drawable.humidity, humidity, "Humidity"))
        return weatherlist
    }
}