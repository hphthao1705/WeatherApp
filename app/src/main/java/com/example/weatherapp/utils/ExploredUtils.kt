package com.example.weatherapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
}