package com.example.weatherapp.repository

import android.app.Application
import com.example.weatherapp.data.local.Search
import com.example.weatherapp.data.local.SearchDatabase
import com.example.weatherapp.data.local.SearchDAO

class SearchRepository(app: Application) {
    private val dao: SearchDAO
    init {
        val searchDatabase: SearchDatabase = SearchDatabase.getInstance(app)
        dao = searchDatabase.getSearchDao()
    }
    fun insertCity(city: Search) = dao.insertCity(city)
    fun getAllCity(): List<Search> = dao.getAllCity()
    fun deleteCity(city: String) = dao.deleteCity(city)
}