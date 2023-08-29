package com.example.weatherapp.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherProperties
import com.example.weatherapp.databinding.ActivityCityScreenBinding
import com.example.weatherapp.view.adapter.WeatherAdapter
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL


class ActivityCityScreen : AppCompatActivity() {
    private lateinit var binding:ActivityCityScreenBinding
    private val viewModel by viewModels<WeatherViewModel>()
    private var list_properties:ArrayList<WeatherProperties> = ArrayList(emptyList())
    private var adapter:WeatherAdapter = WeatherAdapter(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_screen)
        var cityName = intent.getStringExtra("cityname")
        loadData(cityName!!)
        binding.imgBack.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            return BitmapFactory.decodeStream(URL(src).openConnection().getInputStream())
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Exception", e.message!!)
            return null
        }
    }

    fun loadData(cityName:String)
    {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        lifecycleScope.launch {
            viewModel.loadWeather(cityName)
            viewModel._liveData.observe(this@ActivityCityScreen)
            {
                binding.location = it.location
                binding.condition = it.current?.condition
                binding.current = it.current
                binding.imgIcon.setImageBitmap(getBitmapFromURL("https:" + it.current?.condition?.icon))
                loadWeatherProperties(
                    it.current?.feelslike_c.toString().trim(),
                    it.current?.wind_kph.toString().trim(),
                    it.current?.humidity.toString().trim()
                )
            }
        }
    }

    fun loadWeatherProperties(feelslike:String, wind:String, humidity:String)
    {
        list_properties.add(WeatherProperties(R.drawable.umbrella, feelslike,"Rain"))
        list_properties.add(WeatherProperties(R.drawable.wind, wind,"Wind"))
        list_properties.add(WeatherProperties(R.drawable.humidity, humidity,"Humidity"))

        binding.recyclerviewWeather3.layoutManager = LinearLayoutManager(this@ActivityCityScreen,LinearLayoutManager.HORIZONTAL, false)
        adapter.setData(list_properties)
        binding.recyclerviewWeather3.adapter =  adapter
    }
}