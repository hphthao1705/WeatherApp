package com.example.weatherapp.repository

import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.remote.APIService
import com.example.weatherapp.utils.Constants
import kotlinx.coroutines.flow.flow
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

val weatherAppModule = module {
    single{
        Retrofit.Builder().
        baseUrl(Constants.BASE_URL).
        addConverterFactory(GsonConverterFactory.create()).
        build().
        create(APIService::class.java)
    }
    single {
        WeatherRepository(get())
        CityRepository()
    }
//    single{
//        Retrofit.Builder().baseUrl(Constants.BASE_URL_CITY).addConverterFactory(GsonConverterFactory.create()).build().create(APIService::class.java)=
//    }
//    single{
//
//    }
}

class WeatherRepository(private val apiService: APIService) : LoadAPIWeather {
    //private val apiService: APIService = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(APIService::class.java)
    override suspend fun loadWeather(cityName: String): Weather? {
        try
        {
            val weather = apiService.getWeather(Constants.KEY, cityName, "no").await()
            weather?.let {
                return weather
            }
        }
        catch(ex:Exception)
        {
            print(ex.message.toString())
        }
        return null
    }

    fun error400(cityName: String) = flow {
        try {
            apiService.getWeather(Constants.KEY, cityName, "no").await()
        } catch (ex: Exception) {
            emit(ex.message.toString())
        }
    }

}








