package com.example.weatherapp.koin.module

import com.example.weatherapp.data.remote.APIService_City
import com.example.weatherapp.data.remote.WeatherAPI
import com.example.weatherapp.utils.Constants
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module{
    single{
        Retrofit.Builder().
        baseUrl(Constants.BASE_URL).
        addConverterFactory(GsonConverterFactory.create()).
        build().
        create(WeatherAPI::class.java)
    }
    single{
        Retrofit.Builder().
        baseUrl(Constants.BASE_URL_CITY).
        addConverterFactory(GsonConverterFactory.create()).
        build().
        create(APIService_City::class.java)
    }
}