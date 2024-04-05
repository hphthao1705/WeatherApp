package com.example.weatherapp.view.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.IOException
import java.net.URL

class FragmentHome : Fragment() {
    companion object {
        fun newInstance(
            cityName: String,
            error: Boolean = false
        ): FragmentHome {
            val fragment = FragmentHome()
            val bundle = Bundle()
            bundle.putBoolean("error", error)
            bundle.putString("cityName", cityName)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: WeatherViewModel

    //private val viewModel:WeatherViewModel by inject()
    //private val viewModel by activityViewModels<WeatherViewModel>()
    private var listProperties: ArrayList<WeatherProperties> = ArrayList(emptyList())
    private var adapter: WeatherAdapter = WeatherAdapter(emptyList())
    private val roomData by activityViewModels<SearchViewModel>()
    private lateinit var viewModelSearch: SearchViewModel
    private var listRoom: ArrayList<Search> = ArrayList()
    private lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        viewModelSearch = getViewModel()

        val error = requireArguments().getBoolean("error")
        val cityName = requireArguments().getString("cityName")

        if (error) {
            isError(View.VISIBLE)
        } else {
            mainActivity.showOrHideLoader(View.VISIBLE)
            loadDataFromRoom()
            saveCurrentState(cityName.orEmpty())
            loadData(cityName.orEmpty())
        }
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

    private fun loadData(cityName: String) {
        //fix loi hinh
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        //xu ly
        lifecycleScope.launch {
            viewModel.loadWeather(cityName)
            viewModel.weatherVisibility.observe(viewLifecycleOwner) {
                if (it == View.VISIBLE) {
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

                    val predicate: (Search) -> Boolean = { it.name == cityName }
                    if (listRoom.any(predicate)) {
                        roomData.deleteCity(cityName)
                    }
                    addCityToRoom(cityName)
                }
            }
            mainActivity.showOrHideLoader(View.GONE)
        }
    }

    private fun loadWeatherProperties(feelslike: String, wind: String, humidity: String) {
        listProperties.add(WeatherProperties(R.drawable.umbrella, feelslike, "Rain"))
        listProperties.add(WeatherProperties(R.drawable.wind, wind, "Wind"))
        listProperties.add(WeatherProperties(R.drawable.humidity, humidity, "Humidity"))

        binding.recyclerviewWeather3.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        adapter.setData(listProperties)
        binding.recyclerviewWeather3.adapter = adapter
    }

    private fun saveCurrentState(cityName: String) {
        val activity: MainActivity? = activity as MainActivity

        val sharedPref = activity?.getSharedPreferences("currentState", MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("state", "City")
            putString("city", cityName)
            commit()
        }
    }

    private fun addCityToRoom(cityName: String) {
        var p = Search(cityName)
        roomData.addNewCity(p)
    }

    private fun loadDataFromRoom() {
        viewModelSearch.getAllNote().observe(this) {
            listRoom = it as ArrayList<Search>
        }
    }

    fun isError(result: Int) {
        viewModel.errorVisibility.value = result
        if (result == View.GONE) {
            viewModel.weatherVisibility.value = View.VISIBLE
        } else {
            viewModel.weatherVisibility.value = View.GONE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
}