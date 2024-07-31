package com.example.weatherapp.utils

import com.example.weatherapp.viewmodel.uiViewModel.CityUIViewModel

object AppUtils {
    private var cityListData: ArrayList<CityUIViewModel> = arrayListOf()
    fun saveListCity(list: List<CityUIViewModel?>) {
        if(list.isNotEmpty()) {
            cityListData.clear()
            cityListData.addAll(list.filterNotNull())
        }
    }

    fun getListCity(): ArrayList<CityUIViewModel> {
        return cityListData
    }

    fun checkListCity(): Boolean {
        return cityListData.size != 0
    }
}