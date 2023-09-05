package com.example.weatherapp.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search")
class Search(@ColumnInfo(name = "City") val name:String?) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ID") var id:Int = 0
}