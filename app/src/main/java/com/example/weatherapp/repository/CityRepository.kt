package com.example.weatherapp.repository

import arrow.core.Either
import com.example.weatherapp.data.base.ErrorData
import com.example.weatherapp.data.model.city.City
import com.example.weatherapp.data.model.city2.Country
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun loadCity() : Flow<Either<ErrorData, City?>>

    fun loadCity2() : Flow<Either<ErrorData, List<Country>>>
}