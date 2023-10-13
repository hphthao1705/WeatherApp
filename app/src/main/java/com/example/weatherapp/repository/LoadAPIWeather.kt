package com.example.weatherapp.repository

import com.example.weatherapp.data.model.Weather

interface LoadAPIWeather {
    suspend fun loadWeather(cityName:String) : Weather?
}