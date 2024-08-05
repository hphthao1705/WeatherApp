package com.example.weatherapp.koin.module

import com.example.weatherapp.view.activity.MainActivityViewModel
import com.example.weatherapp.view.city.CityViewModel
import com.example.weatherapp.view.displayWeather.DisplayWeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel {
        CityViewModel(get())
    }
//
//    viewModel {
//        SearchText()
//    }

//    viewModel{
//        SearchViewModel(androidApplication())
//    }

    viewModel {
        MainActivityViewModel(get())
    }
    viewModel {
        DisplayWeatherViewModel(get())
    }
//    viewModel{
//        SearchViewModel(get())
//    }
}