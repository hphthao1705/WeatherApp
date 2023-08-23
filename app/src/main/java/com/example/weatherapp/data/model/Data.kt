package com.example.weatherapp.data.model

data class Data(
    val city: String,
    val country: String,
    val populationCounts: List<PopulationCount>
)