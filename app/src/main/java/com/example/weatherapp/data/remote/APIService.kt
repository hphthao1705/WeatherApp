package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface APIService {
    @GET(Constants.END_POINT)
    fun getWeather(@Header("key") key:String,
                   @Query("q") cityName:String,
                   @Query("aqi") aqi:String): Call<Weather>

    @GET(Constants.END_POINT_CITY)
    fun getCity(): Call<City>
}