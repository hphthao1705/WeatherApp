package com.example.weatherapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherProperties(
    val image: Int? = 0,
    val value: String? = "",
    val property: String? = ""
) : Parcelable