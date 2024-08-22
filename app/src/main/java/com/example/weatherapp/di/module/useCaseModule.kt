package com.example.weatherapp.di.module

import com.example.weatherapp.data.usecase.CityUseCase
import com.example.weatherapp.data.usecase.WeatherUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { WeatherUseCase(get()) }

    single { CityUseCase(get()) }
}