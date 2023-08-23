package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.repository.CityRepository
import kotlinx.coroutines.launch

class CityViewModel: ViewModel() {
    private val repository: CityRepository = CityRepository()
    var list:ArrayList<Data> = ArrayList(emptyList())

    private var liveData: MutableLiveData<List<Data>> = MutableLiveData()
    var _liveData = liveData

//    private var liveData
    suspend fun loadCities()
    {
        viewModelScope.launch {
            try {
                var flow = repository.loadCity()
                flow.collect(){
                    it?.let {
                        list.add(it)
                    }
                }
                liveData.postValue(list)
            }catch (ex:Exception)
            {
                return@launch error(ex.message ?: ex.toString())
            }
        }
    }
}