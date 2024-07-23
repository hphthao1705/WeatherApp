package com.example.weatherapp.utils

import com.example.weatherapp.data.model.Data

object AppUtils {
    private var cityListData: ArrayList<Data> = arrayListOf()
    fun saveListCity(list: List<Data>) {
        cityListData.clear()
        cityListData.addAll(list)
    }

    fun getListCity() : ArrayList<Data> {
        return cityListData
    }
}