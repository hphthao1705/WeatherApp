package com.example.weatherapp.koin.module

import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.DisplayWeatherViewModel
import com.example.weatherapp.viewmodel.MainActivityViewModel
import com.example.weatherapp.viewmodel.SearchText
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel {
        CityViewModel(get())
    }

    viewModel{
        SearchText()
    }

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