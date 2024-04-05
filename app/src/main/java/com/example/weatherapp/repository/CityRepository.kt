package com.example.weatherapp.repository

import com.example.weatherapp.data.remote.APIService_City
import com.example.weatherapp.data.remote.APIService_Weather
import com.example.weatherapp.utils.Constants
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class CityRepository(private val apiserviceCity: APIService_City) {
    //private val apiService: APIService_City = Retrofit.Builder().baseUrl(Constants.BASE_URL_CITY).addConverterFactory(GsonConverterFactory.create()).build().create(APIService_City::class.java)
    suspend fun loadCity() = flow launch@{
        try {
            val user = apiserviceCity.getCity().awaitResponse()
            if (user.isSuccessful)
            {
                var list = user.body()
                list?.let {
                    emit(list.data)
                }
            }
        }
        catch (ex:Exception)
        {
            print(ex.toString())
        }
    }
    fun error400() = flow {
        try {
            apiserviceCity.getCity().awaitResponse()

        }
        catch (ex:Exception)
        {
            emit(ex.message.toString())
        }
    }
}