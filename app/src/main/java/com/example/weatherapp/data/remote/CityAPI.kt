package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.City
import com.example.weatherapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface CityAPI {
    @GET(Constants.END_POINT_CITY)
    suspend fun getCity(): Response<City>
}