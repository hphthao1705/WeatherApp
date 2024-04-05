package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val doneAPI: MutableLiveData<Boolean> = MutableLiveData()
}