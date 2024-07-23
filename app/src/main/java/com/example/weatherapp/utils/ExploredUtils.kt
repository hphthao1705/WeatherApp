package com.example.weatherapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherProperties
import java.io.IOException
import java.net.URL


object ExploredUtils {
    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            return BitmapFactory.decodeStream(URL(src).openConnection().getInputStream())
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Exception", e.message!!)
            return null
        }
    }

    fun loadWeatherProperties(
        feelslike: String,
        wind: String,
        humidity: String
    ): ArrayList<WeatherProperties> {
        val weatherlist: ArrayList<WeatherProperties> = arrayListOf()
        weatherlist.add(WeatherProperties(R.drawable.umbrella, feelslike, "Rain"))
        weatherlist.add(WeatherProperties(R.drawable.wind, wind, "Wind"))
        weatherlist.add(WeatherProperties(R.drawable.humidity, humidity, "Humidity"))
        return weatherlist
    }
}