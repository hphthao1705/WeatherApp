package com.example.weatherapp.utils

import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.view.city.uiViewModel.CityUIViewModel

object AppUtils {
    var cityListData: MutableLiveData<ArrayList<CityUIViewModel>> = MutableLiveData(
        arrayListOf()
    )
    var isCheckNewLocation: Boolean = true
    fun saveListCity(list: List<CityUIViewModel?>) {
        if(list.isNotEmpty()) {
            cityListData.value?.clear()
            cityListData.value?.addAll(list.filterNotNull())
        }
    }

    fun getListCity(): ArrayList<CityUIViewModel>? {
        return cityListData.value
    }

    fun checkListCity(): Boolean {
        return cityListData.value?.size != 0
    }
}