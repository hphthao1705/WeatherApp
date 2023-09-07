package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.local.dao.SearchDAO
import com.example.weatherapp.data.local.entities.Search

@Database(entities = [Search::class], version = 4)
abstract class SearchDatabase: RoomDatabase() {
    abstract fun getSearchDao(): SearchDAO
    companion object{
        @Volatile
        private var instance:SearchDatabase?=null

        fun getInstance(context: Context):SearchDatabase{
            if(instance == null)
            {
                //instance = Room.databaseBuilder(context,SearchDatabase::class.java,"search").allowMainThreadQueries().build() (not recommend - block UI)
                //instance = Room.databaseBuilder(context,SearchDatabase::class.java,"search").fallbackToDestructiveMigration().build()
                instance = Room.databaseBuilder(context,SearchDatabase::class.java,"search").fallbackToDestructiveMigrationOnDowngrade().build()
            }
            return instance!!
        }
    }
}