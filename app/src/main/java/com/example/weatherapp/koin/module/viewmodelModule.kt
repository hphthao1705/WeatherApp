package com.example.weatherapp.koin.module

import android.app.Application
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.SearchText
import com.example.weatherapp.viewmodel.SearchViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel{
        WeatherViewModel(get())
    }

    viewModel {
        CityViewModel(get())
    }

    viewModel{
        SearchText()
    }
    viewModel{
        SearchViewModel(get())
    }
}