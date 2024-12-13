package com.example.weatherapp.utils

import android.app.Application
import android.content.SharedPreferences
import com.example.weatherapp.data.model.CityData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Prefs {
    private lateinit var application: Application
    private lateinit var prefs: SharedPreferences
    private const val CURENT_STATE = "currentState"
    private const val CITY = "city"
    private const val PREFS_FILENAME = "PrefsData"
    private const val PERMISSION_TYPE = "permission_type"
    private const val LATITUDE = "latitude"
    private const val LONGITUDE = "longitude"

    fun init(application: Application) {
        this.application = application
        prefs = Prefs.application.getSharedPreferences(PREFS_FILENAME, 0)

    }

    var permissionType: String
        get() = prefs.getString(PERMISSION_TYPE, "").toString()
        set(value) = prefs.edit().putString(PERMISSION_TYPE, value).apply()

    var latitude: String
        get() = prefs.getString(LATITUDE, "0").toString()
        set(value) = prefs.edit().putString(LATITUDE, value).apply()
    var longitude: String
        get() = prefs.getString(LONGITUDE, "0").toString()
        set(value) = prefs.edit().putString(LONGITUDE, value).apply()

    fun saveCurrentState(cityName: String) {
        val prefsEditor = prefs.edit()
        cityName?.let {

        } ?: run {
            prefsEditor.putString(CURENT_STATE, cityName)
        }
        prefsEditor.apply()
    }

    fun getCurrentState(): String {
        val prefsReader = prefs
        val city = prefsReader.getString(CURENT_STATE, "")
        city?.let {
            return it
        }
        return ""
    }

    fun clearCurrentState() {
        val prefsEditor = prefs.edit()
        prefsEditor.remove(CURENT_STATE)
        prefsEditor.apply()
    }

    fun saveCity(cityData: CityData?) {
        val prefsEditor = prefs.edit()
        cityData?.let {
            val gson = Gson()
            val json = gson.toJson(cityData)
            prefsEditor.putString(CITY, json)
        } ?: run {
            prefsEditor.putString(CITY, "")
        }
        prefsEditor.apply()
    }

    fun getCityData(): CityData {
        val mPrefs = prefs
        val gson = Gson()
        val json = mPrefs.getString(CITY, "")
        return if (json.isNullOrEmpty()) {
            CityData()
        } else {
            val typeMyType: Type = object : TypeToken<ArrayList<CityData?>?>() {}.getType()
            gson.fromJson<CityData>(json, typeMyType)
        }
    }
}