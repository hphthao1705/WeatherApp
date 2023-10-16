package com.example.weatherapp.koin

import com.example.weatherapp.data.remote.APIService
import com.example.weatherapp.repository.CityRepository
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//val weatherAppModule = module {
//    single{
//        Retrofit.Builder().
//        baseUrl(Constants.BASE_URL).
//        addConverterFactory(GsonConverterFactory.create()).
//        build().
//        create(APIService::class.java)
//    }
//    single<WeatherRepository> {
//        WeatherRepository(get())
//    }
//    viewModel<WeatherViewModel>{
//        WeatherViewModel(get())
//    }
//}

val cityModule = module {
    viewModel {
        CityViewModel()
    }
    viewModel{
        WeatherViewModel(get())
    }
}