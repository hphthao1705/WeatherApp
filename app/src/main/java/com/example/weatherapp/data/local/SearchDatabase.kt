package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Search::class], version = 7, exportSchema = false)
abstract class SearchDatabase: RoomDatabase() {
    abstract fun getSearchDao(): SearchDAO
    companion object{
        @Volatile
        private var instance:SearchDatabase?=null

        fun getInstance(context: Context):SearchDatabase{
            if(instance == null)
            {
                instance = Room.databaseBuilder(context,SearchDatabase::class.java,"search").allowMainThreadQueries().build()
                //muốn database mới khi đã có database rồi thì xài câu lệnh này
//                instance = Room.databaseBuilder(context,SearchDatabase::class.java,"search").fallbackToDestructiveMigration().build()
//                instance = Room.databaseBuilder(context,SearchDatabase::class.java,"search").fallbackToDestructiveMigrationOnDowngrade().build()
            }
            return instance!!
        }
    }
}