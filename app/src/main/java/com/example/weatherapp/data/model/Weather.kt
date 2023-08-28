package com.example.weatherapp.data.model

data class Weather(
    val error: Error,
    val current: Current?,
    val location: Location?
)