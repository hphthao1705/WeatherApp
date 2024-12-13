package com.example.weatherapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class CityData(
    val city: String? = "",
    val lat: Double? = 0.0,
    val long: Double? = 0.0
) : Parcelable