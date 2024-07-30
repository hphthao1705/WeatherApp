package com.example.weatherapp.koin.module

import com.example.weatherapp.data.remote.CityAPI
import com.example.weatherapp.data.remote.CityAPI2
import com.example.weatherapp.data.remote.WeatherAPI
import com.example.weatherapp.repository.CityRepository
import com.example.weatherapp.repository.CityRepositoryImp
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.repository.WeatherRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
//    single {
//        SearchRepository(Application(), get())
//    }

    fun provideWeatherRepository(
        weatherAPI: WeatherAPI
    ): WeatherRepository {
        return WeatherRepositoryImp(weatherAPI)
    }

    single { provideWeatherRepository(get()) }

    fun provideCityRepository(
        cityAPI: CityAPI,
        cityAPI2: CityAPI2
    ): CityRepository {
        return CityRepositoryImp(cityAPI = cityAPI, cityAPI2 = cityAPI2)
    }

    single { provideCityRepository(get(), get()) }
}