package com.example.weatherapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.utils.AppUtils

class CityViewModel(): ViewModel() {
    var errorVisibility: MutableLiveData<Int> = MutableLiveData()

    init {
        checkListCityData()
    }

    fun checkListCityData() {
        if(AppUtils.checkListCity()) {
            errorVisibility.postValue(View.GONE)
        } else {
            errorVisibility.postValue(View.VISIBLE)
        }
    }
}