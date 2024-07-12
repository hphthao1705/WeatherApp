package com.example.weatherapp.utils

import android.util.Log
import com.example.weatherapp.data.model.Data

object AppUtils {
    private var cityListData: ArrayList<Data> = arrayListOf()
    fun saveListCity(list: List<Data>) {
        Log.d("Thao Ho", "saveListCity" + list.toString())
        cityListData.clear()
        cityListData.addAll(list)
        Log.d("Thao Ho", "saveListCity" + cityListData.toString())
    }

    fun getListCity() : ArrayList<Data> {
        return cityListData
    }
}