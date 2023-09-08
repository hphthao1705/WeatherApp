package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.repository.CityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel: ViewModel() {
    private val repository: CityRepository = CityRepository()
    var list:ArrayList<Data> = ArrayList(emptyList())

    private var liveData: MutableLiveData<List<Data>> = MutableLiveData()
    var _liveData = liveData

    private var errorMessage:String = ""
    suspend fun loadCities(): String
    {
        try {
            viewModelScope.launch {
                val flow = repository.loadCity()
                flow.collect(){
                    liveData.postValue(it)
                }
                repository.error400().collect {
                    errorMessage = it
                }
            }
        }
        catch(ex:Exception)
        {
            println(ex.message.toString())
        }
        return errorMessage
    }
}