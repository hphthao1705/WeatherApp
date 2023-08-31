package com.example.weatherapp.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherProperties
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.WeatherAdapter
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL

class FragmentHome(val cityName:String) : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<WeatherViewModel>()
    private var list_properties:ArrayList<WeatherProperties> = ArrayList(emptyList())
    private var adapter: WeatherAdapter = WeatherAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData(cityName)
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
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        lifecycleScope.launch {
            viewModel.loadWeather(cityName)
            viewModel._liveData.observe(viewLifecycleOwner)
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

        binding.recyclerviewWeather3.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        adapter.setData(list_properties)
        binding.recyclerviewWeather3.adapter =  adapter
    }
}