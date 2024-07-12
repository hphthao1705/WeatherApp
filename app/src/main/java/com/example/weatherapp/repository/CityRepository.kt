package com.example.weatherapp.repository

import com.example.weatherapp.data.remote.APIService_City
import kotlinx.coroutines.flow.flow
import retrofit2.awaitResponse


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
}