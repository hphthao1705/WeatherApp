package com.example.weatherapp.utils

import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.viewmodel.uiViewModel.WeatherUIViewModel

object WeatherMapping {
    fun mapWeatherDataToUI(weather: Weather) : WeatherUIViewModel {
        val result = WeatherUIViewModel(
            city = weather.location?.name.orEmpty(),
            image = weather.current?.condition?.icon.orEmpty(),
            condition = weather.current?.condition?.text.orEmpty(),
            tempurature = weather.current?.temp_c.orEmpty(),
            properties =  WeatherUtils.loadWeatherProperties(
                feelslike = weather.current?.feelslike_c.toString().orEmpty(),
                wind = weather.current?.wind_dir.orEmpty(),
                humidity = weather.current?.humidity.toString().orEmpty()
            )
        )
        return result
    }
}