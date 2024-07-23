package com.example.weatherapp.repository

//import android.provider.SyncStateContract.Constants
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.example.weatherapp.data.base.ApiHelper
import com.example.weatherapp.data.base.ErrorData
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.remote.WeatherAPI
import com.example.weatherapp.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WeatherRepositoryImp(private val weatherAPI: WeatherAPI) : WeatherRepository {
    //private val apiService: APIService = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(APIService::class.java)
//    override suspend fun loadWeather(cityName: String): Weather? {
//        try {
//            val weather = apiService.getWeather(Constants.KEY, cityName, "no").await()
//            weather?.let {
//                return weather
//            }
//        } catch (ex: Exception) {
//            print(ex.message.toString())
//        }
//        return null
//    }
    override fun loadWeather(cityName: String): Flow<Either<ErrorData, Weather?>> = flow {
        ApiHelper.launch(
            apiCall = { weatherAPI.getWeather(key = Constants.KEY, cityName = cityName, "no") },
            onSuccess = { result -> emit((result).right()) },
            catchOnHttpError = {
                emit(it.left())
            }
        )
    }
}








