package com.example.weatherapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.utils.AppUtils

class CityViewModel(): ViewModel() {
    var errorVisibility: MutableLiveData<Int> = MutableLiveData()

    fun checkListCityData() {
        AppUtils.getListCity()?.let {
            errorVisibility.postValue(View.GONE)
        } ?: run {
            errorVisibility.postValue(View.VISIBLE)
        }
    }
}