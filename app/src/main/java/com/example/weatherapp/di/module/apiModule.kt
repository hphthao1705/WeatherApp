package com.example.weatherapp.di.module

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.remote.CityAPI
import com.example.weatherapp.data.remote.CityAPI2
import com.example.weatherapp.data.remote.WeatherAPI
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module{
    single{
        Retrofit.Builder().
        baseUrl(BuildConfig.WEATHER_URL).
        addConverterFactory(GsonConverterFactory.create()).
        build().
        create(WeatherAPI::class.java)
    }
    single{
        Retrofit.Builder().
        baseUrl(BuildConfig.CITY_URL).
        addConverterFactory(GsonConverterFactory.create()).
        build().
        create(CityAPI::class.java)
    }
    single{
        Retrofit.Builder().
        baseUrl(BuildConfig.CITY_URL_2).
        addConverterFactory(GsonConverterFactory.create()).
        build().
        create(CityAPI2::class.java)
    }
}