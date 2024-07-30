package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.city2.Country
import com.example.weatherapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface CityAPI2 {
    @GET(Constants.END_POINT_CITY2)
    suspend fun getCity2(): Response<ArrayList<Country>>
}