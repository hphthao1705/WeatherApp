package com.example.weatherapp.repository

import android.app.Application
import com.example.weatherapp.data.local.Search
import com.example.weatherapp.data.local.SearchDAO
import com.example.weatherapp.data.local.SearchDatabase

class SearchRepository(app: Application) {
    private val dao: SearchDAO
    init {
        val searchDatabase: SearchDatabase = SearchDatabase.getInstance(app)
        dao = searchDatabase.getSearchDao()
    }
    fun insertCity(city: Search) = dao.insertCity(city)
    fun getAllCity(): List<Search> = dao.getAllCity()
    fun deleteCity(city: String) = dao.deleteCity(city)
    fun checkExistence(city: String) : Int {
        return dao.checkExistence(city)
    }
}