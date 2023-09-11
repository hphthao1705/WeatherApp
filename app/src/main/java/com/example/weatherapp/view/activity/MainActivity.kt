package com.example.weatherapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.view.fragment.FragmentEmptyStateCity
import com.example.weatherapp.view.fragment.FragmentCity
import com.example.weatherapp.view.fragment.FragmentEmptyFavouriteCity
import com.example.weatherapp.view.fragment.FragmentFavouriteCity
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        replaceFragment(FragmentLoading())
        loadCities()
        loadDataFromRoom()
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
            if(listRoom.isEmpty())
            {
                replaceFragment(FragmentEmptyFavouriteCity())
            }
            else
            {
                replaceFragment(FragmentFavouriteCity())
            }
        }
        searchCity()
        loadScreenWhenAppStart()
    }
    fun loadScreenWhenAppStart()
    {
        if(listRoom.isNotEmpty())
        {
            replaceFragment(FragmentFavouriteCity())
        }
        else
        {
            if(listData.isNotEmpty())
            {
                replaceFragment(FragmentCity(listData,listRoom))
            }
            else
            {
                replaceFragment(FragmentEmptyStateCity())
            }
        }
    }
    fun searchCity():String
    {
        var textSearch = ""
        binding.txtSearch.doAfterTextChanged {
            textSearch = it.toString()
            if(textSearch.equals("", true))
            {
                replaceFragment(FragmentCity(listData,listRoom))
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
        }
    }
}