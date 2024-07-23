package com.example.weatherapp.repository

import arrow.core.Either
import com.example.weatherapp.data.base.ErrorData
import com.example.weatherapp.data.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
//    suspend fun loadWeather(cityName:String) : Weather?

    fun loadWeather(cityName:String) : Flow<Either<ErrorData, Weather?>>
}