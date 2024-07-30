package com.example.weatherapp.data.model.city2

import com.example.weatherapp.data.model.city2.NativeName

data class Name(
    val common: String,
    val official: String,
    val nativeName: Map<String, NativeName>
)