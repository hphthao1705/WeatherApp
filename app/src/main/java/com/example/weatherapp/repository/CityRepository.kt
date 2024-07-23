package com.example.weatherapp.repository

import arrow.core.Either
import com.example.weatherapp.data.base.ErrorData
import com.example.weatherapp.data.model.City
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun loadCity() : Flow<Either<ErrorData, City?>>
}