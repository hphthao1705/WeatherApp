package com.example.weatherapp.view.displayWeather

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.usecase.WeatherUseCase
import com.example.weatherapp.view.displayWeather.adapter.WeatherAdapter
import com.example.weatherapp.view.displayWeather.uiViewModel.WeatherUIViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayWeatherViewModel(private val weatherUseCase: WeatherUseCase) : ViewModel() {
    var weather: WeatherUIViewModel = WeatherUIViewModel()
    val loadingVisibility: MutableLiveData<Int> = MutableLiveData()
    val city: MutableLiveData<String> = MutableLiveData()
    val condition: MutableLiveData<String> = MutableLiveData()
    val tempurature: MutableLiveData<String> = MutableLiveData()
    val imageWeather: MutableLiveData<String> = MutableLiveData()
    val isSuccessful: MutableLiveData<Boolean> = MutableLiveData()
    val dataVisibility: MutableLiveData<Int> = MutableLiveData()
    val weatherAdapter = WeatherAdapter()
    //error
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorTitle: MutableLiveData<String> = MutableLiveData("NO RESULTS")
    val errorContent: MutableLiveData<String> = MutableLiveData("Sorry, there are no results for this search.\nPlease try another place")
    //action
    val mapClick: MutableLiveData<Boolean> = MutableLiveData()
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

    init {
        dataVisibility.value = View.VISIBLE
        errorVisibility.value = View.GONE
    }

    fun loadWeather(cityName: String) {
        coroutineScopeIO.launch {
            weatherUseCase.getWeather(cityName).collect {
                it.onRight {data ->
                    data?.let {
                        weather = it
                        withContext(Dispatchers.Main) {
                            city.value = data.city.orEmpty()
                            condition.value = data.condition.orEmpty()
                            tempurature.value = data.tempurature.orEmpty()
                            imageWeather.value = "https:${data.image}"
                            data.properties?.let {
                                weatherAdapter.setData(it)
                            } ?: run {
                                weatherAdapter.setData(arrayListOf())
                            }
                        }
                        weather = it
                    }
                }
                it.onLeft {
                    errorVisibility.postValue(View.VISIBLE)
                }
            }
            isSuccessful.postValue(true)
        }
    }

    fun onMapClick() {
        mapClick.value = true
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScopeIO.cancel()
    }
}