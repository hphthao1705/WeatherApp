package com.example.weatherapp.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.weatherapp.data.base.ApiHelper
import com.example.weatherapp.data.base.ErrorData
import com.example.weatherapp.data.model.city.City
import com.example.weatherapp.data.model.city2.Country
import com.example.weatherapp.data.remote.CityAPI
import com.example.weatherapp.data.remote.CityAPI2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CityRepositoryImp(private val cityAPI: CityAPI, private val cityAPI2: CityAPI2) : CityRepository {
    //private val apiService: APIService_City = Retrofit.Builder().baseUrl(Constants.BASE_URL_CITY).addConverterFactory(GsonConverterFactory.create()).build().create(APIService_City::class.java)
    override fun loadCity(): Flow<Either<ErrorData, City?>> = flow {
        ApiHelper.launch(
            apiCall = { cityAPI.getCity() },
            onSuccess = { result -> emit((result).right()) },
            catchOnHttpError = {
                emit(it.left())
            }
        )
    }

    override fun loadCity2(): Flow<Either<ErrorData, List<Country>>> = flow {
        ApiHelper.launch(
            apiCall = { cityAPI2.getCity2() },
            onSuccess = { result -> emit((result?.toList().orEmpty()).right()) },
            catchOnHttpError = {
                emit(it.left())
            }
        )
    }
}