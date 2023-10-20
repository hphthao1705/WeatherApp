package com.example.weatherapp.koin.module

import android.app.Application
import com.example.weatherapp.repository.CityRepository
import com.example.weatherapp.repository.SearchRepository
import com.example.weatherapp.repository.WeatherRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        WeatherRepository(get())
    }
    single {
        CityRepository(get())
    }
//    single {
//        SearchRepository(Application(), get())
//    }
}