package com.example.weatherapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.local.entities.Search

@Dao
interface SearchDAO {
    @Query("select distinct City, id from search order by id desc limit 10;")
    fun getAllCity(): LiveData<List<Search>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCity(city: Search)
    @Query("delete from search where city = :city")
    fun deleteCity(city: String)
}