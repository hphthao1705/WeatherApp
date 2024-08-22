package com.example.weatherapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search")
data class Search(
    @ColumnInfo(name = "City") val name: String?,
    @ColumnInfo(name = "Image") val image: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}