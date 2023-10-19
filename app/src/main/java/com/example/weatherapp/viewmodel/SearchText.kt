package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchText: ViewModel() {
    private var _liveData: MutableLiveData<Boolean>? = MutableLiveData()
    val LiveData = _liveData
    fun searchDone()
    {
        _liveData?.value = true
    }
}