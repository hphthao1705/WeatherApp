package com.example.weatherapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.weatherapp.data.local.SearchDatabase
import com.example.weatherapp.data.local.dao.SearchDAO
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.City

class SearchRepository(app: Application, private var dao: SearchDAO) {
    //private val dao:SearchDAO
    init {
        val searchDatabase: SearchDatabase = SearchDatabase.getInstance(app)
        dao = searchDatabase.getSearchDao()
    }
    fun insertCity(city: Search) = dao.insertCity(city)
    fun getAllCity(): LiveData<List<Search>> = dao.getAllCity()
    fun deleteCity(city: String) = dao.deleteCity(city)
}