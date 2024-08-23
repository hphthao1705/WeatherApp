package com.example.weatherapp.data.model.city

data class City(
    val `data`: List<Data>,
    val error: Boolean,
    val msg: String
)