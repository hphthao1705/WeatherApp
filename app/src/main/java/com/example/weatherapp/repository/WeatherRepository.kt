package com.example.weatherapp.repository

import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.remote.APIService
import com.example.weatherapp.utils.Constants
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {
    private val apiService: APIService = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(APIService::class.java)
    suspend fun loadWeather() :Weather? {
        try {
            val weather = apiService.getWeather(Constants.KEY, "VietNam", "no").await()
            weather?.let {
                return weather
            }
        }
        catch (ex: Exception)
        {
            return error(ex.message ?: ex.toString())
        }
        return null
    }
}