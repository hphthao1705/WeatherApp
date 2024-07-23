package com.example.weatherapp.data.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.weatherapp.data.base.ErrorData
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.WeatherMapping
import com.example.weatherapp.viewmodel.uiViewModel.WeatherUIViewModel
import kotlinx.coroutines.flow.channelFlow

class WeatherUseCase(private val weatherRepository: WeatherRepository) {
    suspend fun getWeather(cityName:String) = channelFlow<Either<ErrorData, WeatherUIViewModel?>> {
        try {
            val weather = weatherRepository.loadWeather(cityName)
            weather.collect {
                it.onRight {
                    it?.let {
                        send(WeatherMapping.mapWeatherDataToUI(it).right())
                    }
                }
                it.onLeft {
                    send(it.left())
                }
            }
        } catch (ex: Exception) {
            send(ErrorData(400, "").left())
        }
    }
}