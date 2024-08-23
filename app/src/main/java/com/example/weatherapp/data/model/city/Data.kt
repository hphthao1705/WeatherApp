package com.example.weatherapp.data.model.city

data class Data(
    val city: String,
    val country: String,
    val populationCounts: List<PopulationCount>
)