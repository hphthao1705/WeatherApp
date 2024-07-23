package com.example.weatherapp.koin.module

import com.example.weatherapp.data.usecase.WeatherUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { WeatherUseCase(get()) }
}