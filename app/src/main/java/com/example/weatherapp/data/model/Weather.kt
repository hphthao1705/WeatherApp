package com.example.weatherapp.data.model

import com.example.weatherapp.data.model.city.Location

data class Weather(
    val current: Current?,
    val location: Location?
)