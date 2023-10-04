package com.example.weatherapp.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.data.model.State
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.utils.CountingIdlingResourceSingleton.countingIdlingResource
import com.example.weatherapp.view.fragment.FragmentCity
import com.example.weatherapp.view.fragment.FragmentFavouriteCity
import com.example.weatherapp.view.fragment.FragmentHome
import com.example.weatherapp.view.fragment.FragmentLoading
import com.example.weatherapp.view.fragment.FragmentSearch
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val viewModel by viewModels<CityViewModel>()
    private var listData:ArrayList<Data> = ArrayList()
    private val viewModelSearch: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.ViewModelFactory(this.application)
        )[SearchViewModel::class.java]
    }
    private var listRoom:ArrayList<Search> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        countingIdlingResource.increment()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        replaceFragment(FragmentLoading())
        loadCities()
        loadDataFromRoom()
        countingIdlingResource.decrement()
    }
    fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.framelayout.id, fragment)
        fragmentTransaction.commit()
    }
    fun initControls()
    {
        binding.btnListcity.setOnClickListener{
            replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
        }
        searchCity()
        loadScreenWhenAppStart()
    }
    fun loadScreenWhenAppStart()
    {
        if(listRoom.isNotEmpty())
        {
            replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
        }
        else
        {
            replaceFragment(FragmentCity(listData, listRoom))
        }
    }
    fun searchCity():String
    {
        var textSearch = ""
        binding.txtSearch.doAfterTextChanged {
            textSearch = it.toString()
            if(textSearch.equals("", true))
            {
                val state = getCurrentState()
                when(state.state)
                {
                    "Home" -> replaceFragment(FragmentHome(state.city, listRoom))
                    "FavouriteCity" -> replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
                    "City" -> replaceFragment(FragmentCity(listData, listRoom))
                }
            }
            else
            {
                replaceFragment(FragmentSearch(textSearch, listData, listRoom))
            }
        }
        return textSearch
    }
    fun loadDataFromRoom()
    {
        viewModelSearch.getAllNote().observe(this@MainActivity){
            listRoom = it as ArrayList<Search>
        }
    }
    fun loadCities()
    {
        lifecycleScope.launch {
            viewModel._liveData.observe(this@MainActivity) {
                listData = it as ArrayList<Data>
                initControls()
            }
            viewModel._liveDataAPI?.observe(this@MainActivity){
                initControls()
            }
        }
    }
    private fun getCurrentState(): State {
        val sharedPreferences = getSharedPreferences("currentState", MODE_PRIVATE)
        val s1 = sharedPreferences.getString("state", "null roi be oi :(((")!!
        val s2 = sharedPreferences.getString("city", "null roi be oi :(((")!!
        return State(s1, s2)
    }
}