package com.example.weatherapp.view.city.uiViewModel

import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.data.model.city2.Country

data class CityUIViewModel (
    val city: String,
    val image: String,
) {
    companion object {
        fun from(city: Data) = CityUIViewModel(
            city = city.city,
            image = ""
        )

        fun from(city: Country) = CityUIViewModel(
            city = city.name.common,
            image = city.flags.png
        )

        fun from(search: Search) = CityUIViewModel(
            city = search.name.orEmpty(),
            image = search.image.orEmpty()
        )
    }
}