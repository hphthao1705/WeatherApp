package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.City
import com.example.weatherapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET

interface APIService_City {
    @GET(Constants.END_POINT_CITY)
    fun getCity(): Call<City>
}