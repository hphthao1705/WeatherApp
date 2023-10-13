package com.example.weatherapp.repository

import com.example.weatherapp.data.model.Data
import com.example.weatherapp.data.remote.APIService
import com.example.weatherapp.utils.Constants
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class CityRepository(private val apiService: APIService):LoadAPICity{
    override suspend fun loadCity() = flow launch@{
        try {
            val user = apiService.getCity().awaitResponse()
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
            apiService.getCity().awaitResponse()

        }
        catch (ex:Exception)
        {
            emit(ex.message.toString())
        }
    }
}