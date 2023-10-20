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
import com.example.weatherapp.viewmodel.SearchText
import com.example.weatherapp.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel:CityViewModel
    private var listData:ArrayList<Data> = ArrayList()
    private val viewModelSearch: SearchViewModel by lazy {
        ViewModelProvider(this@MainActivity, SearchViewModel.ViewModelFactory(this.application))[SearchViewModel::class.java]
    }
    private lateinit var searchTextViewModel:SearchText
    private var listRoom:ArrayList<Search> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        searchTextViewModel = getViewModel()
        viewModel = getViewModel()
        //viewModelSearch = getViewModel()
        //countingIdlingResource.increment()
        replaceFragment(FragmentLoading())
        loadCities()
        loadDataFromRoom()
        hearEventSearchFinish()
        //countingIdlingResource.decrement()
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
        //countingIdlingResource.increment()
        binding.btnListcity.setOnClickListener{
            replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
        }
        searchCity()
        loadScreenWhenAppStart()
        //countingIdlingResource.decrement()
    }
    fun loadScreenWhenAppStart()
    {
        //countingIdlingResource.increment()
        if(listRoom.isNotEmpty())
        {
            replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
            //countingIdlingResource.decrement()
        }
        else
        {
            replaceFragment(FragmentCity(listData, listRoom))
            //countingIdlingResource.decrement()
        }
    }
    fun searchCity():String
    {
        //countingIdlingResource.increment()
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
                //countingIdlingResource.decrement()
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
        //countingIdlingResource.increment()
        viewModelSearch.getAllNote().observe(this@MainActivity){
            listRoom = it as ArrayList<Search>
            //countingIdlingResource.decrement()
        }
    }
    fun loadCities()
    {
        //countingIdlingResource.increment()
        lifecycleScope.launch {
            viewModel._liveData.observe(this@MainActivity) {
                listData = it as ArrayList<Data>
                initControls()
            }
            viewModel._liveDataAPI?.observe(this@MainActivity){
                initControls()
            }
            //countingIdlingResource.decrement()
        }
    }
    private fun getCurrentState(): State {
        //countingIdlingResource.increment()
        val sharedPreferences = getSharedPreferences("currentState", MODE_PRIVATE)
        val s1 = sharedPreferences.getString("state", "null roi be oi :(((")!!
        val s2 = sharedPreferences.getString("city", "null roi be oi :(((")!!
        //countingIdlingResource.decrement()
        return State(s1, s2)
    }
    private fun hearEventSearchFinish()
    {
        searchTextViewModel.LiveData?.observe(this){
            binding.txtSearch.text.clear()
        }
    }
}