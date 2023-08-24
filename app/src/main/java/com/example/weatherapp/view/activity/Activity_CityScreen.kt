package com.example.weatherapp.view.activity

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
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityCityScreenBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL


class Activity_CityScreen : AppCompatActivity() {
    private lateinit var binding:ActivityCityScreenBinding
    private val viewModel by viewModels<WeatherViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_screen)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        lifecycleScope.launch{
            viewModel.loadWeather()
            viewModel._liveData.observe(this@Activity_CityScreen)
            {
                binding.location = it.location
                binding.condition = it.current.condition
                binding.current = it.current
                binding.imgIcon.setImageBitmap(getBitmapFromURL("https:" + it.current.condition.icon))
            }
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
}