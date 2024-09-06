package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchDAO {
    @Query("select distinct City, id, Image from search order by id desc limit 10;")
    fun getAllCity(): List<Search>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCity(city: Search)
    @Query("delete from search where city = :city")
    fun deleteCity(city: String)
    @Query("SELECT COUNT(city) FROM search WHERE city = :city")
    fun checkExistence(city: String) : Int
}