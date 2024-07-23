package com.example.weatherapp.koin.module

import com.example.weatherapp.data.remote.WeatherAPI
import com.example.weatherapp.repository.CityRepository
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.repository.WeatherRepositoryImp
import org.koin.dsl.module

val repositoryModule = module {
//    single {
//        WeatherRepository(get())
//    }
    single {
        CityRepository(get())
    }
//    single {
//        SearchRepository(Application(), get())
//    }

    fun provideLoungePassRepository(
        loungePassApi: WeatherAPI
    ): WeatherRepository {
        return WeatherRepositoryImp(loungePassApi)
    }

    single { provideLoungePassRepository(get()) }
}