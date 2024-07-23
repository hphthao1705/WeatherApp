package com.example.weatherapp.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.weatherapp.data.base.ApiHelper
import com.example.weatherapp.data.base.ErrorData
import com.example.weatherapp.data.model.City
import com.example.weatherapp.data.remote.CityAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class CityRepositoryImp(private val cityAPI: CityAPI) : CityRepository {
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

}