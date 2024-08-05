package com.example.weatherapp.view.displayWeather.uiViewModel

import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.WeatherProperties
import com.example.weatherapp.utils.WeatherUtils

data class WeatherUIViewModel (
    val city: String? = "",
    val image: String? = "",
    val condition: String? = "",
    val tempurature: String? = "",
    val properties: ArrayList<WeatherProperties>? = arrayListOf()
) {
    companion object {
        fun from(weather: Weather) = WeatherUIViewModel(
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
    }
}