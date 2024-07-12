package com.example.weatherapp.repository

import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.remote.APIService_Weather
import com.example.weatherapp.utils.Constants
import retrofit2.await

class WeatherRepository(private val apiService: APIService_Weather) : LoadAPIWeather {
    //private val apiService: APIService = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(APIService::class.java)
    override suspend fun loadWeather(cityName: String): Weather? {
        try {
            val weather = apiService.getWeather(Constants.KEY, cityName, "no").await()
            weather?.let {
                return weather
            }
        } catch (ex: Exception) {
            print(ex.message.toString())
        }
        return null
    }
}








