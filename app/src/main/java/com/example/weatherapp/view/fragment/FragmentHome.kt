package com.example.weatherapp.view.fragment

import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.WeatherProperties
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.WeatherAdapter
import com.example.weatherapp.viewmodel.SearchViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL


class FragmentHome(val cityName:String, private var listRoom: List<Search>) : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<WeatherViewModel>()
    private var listProperties:ArrayList<WeatherProperties> = ArrayList(emptyList())
    private var adapter: WeatherAdapter = WeatherAdapter(emptyList())
    private val roomData by activityViewModels<SearchViewModel>()
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
        saveCurrentState()
        loadData(cityName)
    }
    private fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            return BitmapFactory.decodeStream(URL(src).openConnection().getInputStream())
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Exception", e.message!!)
            return null
        }
    }
    private fun loadData(cityName:String)
    {
        //fix loi hinh
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        //xu ly
        lifecycleScope.launch {
            val check = viewModel.loadWeather(cityName)
            //Toast.makeText(context,check,Toast.LENGTH_SHORT).show()
            if (check.isNotEmpty())
            {
                setVisibility(false)
            }
            else
            {
                viewModel._liveData.observe(viewLifecycleOwner)
                { it ->
                    binding.location = it.location
                    binding.condition = it.current?.condition
                    binding.current = it.current
                    binding.imgIcon.setImageBitmap(getBitmapFromURL("https:" + it.current?.condition?.icon))
                    loadWeatherProperties(
                        it.current?.feelslike_c.toString().trim(),
                        it.current?.wind_kph.toString().trim(),
                        it.current?.humidity.toString().trim()
                    )
                    //luu vao Room
                    val predicate: (Search) -> Boolean = {it.name == cityName}
                    if(listRoom.any(predicate))
                    {
                        roomData.deleteCity(cityName)
                    }
                    var p = Search(cityName)
                    roomData.addNewCity(p)
                    setVisibility(true)
                }
            }
            binding.loading.visibility = View.GONE
        }
    }
    private fun loadWeatherProperties(feelslike:String, wind:String, humidity:String)
    {
        listProperties.add(WeatherProperties(R.drawable.umbrella, feelslike,"Rain"))
        listProperties.add(WeatherProperties(R.drawable.wind, wind,"Wind"))
        listProperties.add(WeatherProperties(R.drawable.humidity, humidity,"Humidity"))

        binding.recyclerviewWeather3.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)
        adapter.setData(listProperties)
        binding.recyclerviewWeather3.adapter =  adapter
    }
    private fun saveCurrentState()
    {
        val activity: MainActivity? = activity as MainActivity

        val sharedPref = activity?.getSharedPreferences("currentState", MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("state", "Home")
            putString("city", cityName)
            commit()
        }
    }
    private fun setVisibility(bool: Boolean)
    {
        if(bool)
        {
            binding.cardviewCity.visibility = View.VISIBLE
            binding.empty1.visibility = View.GONE
            binding.empty2.visibility = View.GONE
            binding.empty3.visibility = View.GONE
        }
        else
        {
            binding.cardviewCity.visibility = View.GONE
            binding.empty1.visibility = View.VISIBLE
            binding.empty2.visibility = View.VISIBLE
            binding.empty3.visibility = View.VISIBLE
        }
    }
}