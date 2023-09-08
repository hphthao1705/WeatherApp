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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
    private var list:ArrayList<Search> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        replaceFragment(FragmentLoading())
        prepareData()
    }
    fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.framelayout.id, fragment)
        fragmentTransaction.commit()
    }
    suspend fun initControls()
    {
        delay(5000)
        binding.btnListcity.setOnClickListener{
            if(list.isEmpty())
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
        if(list.isNotEmpty())
        {
            replaceFragment(FragmentFavouriteCity())
        }
        else
        {
            if(listData.isNotEmpty())
            {
                replaceFragment(FragmentCity(listData))
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
            if(textSearch.equals(""))
            {
                replaceFragment(FragmentCity(listData))
            }
            else
            {
                replaceFragment(FragmentSearch(textSearch, listData))
            }
        }
        return textSearch
    }
    fun loadDataFromRoom()
    {
        viewModelSearch.getAllNote().observe(this@MainActivity){
            list = it as ArrayList<Search>
        }
    }
    fun loadCities()
    {
        lifecycleScope.launch {
            if(viewModel.loadCities() == "")
            {
                viewModel._liveData.observe(this@MainActivity)
                {
                    listData = it as ArrayList<Data>
//                    replaceFragment(FragmentCity(it))
                }
            }
        }
    }
    private fun prepareData()
    {
        val job1 = lifecycleScope.launch {
            loadCities()
            loadDataFromRoom()
        }
        runBlocking {
            job1.join()
            launch(Dispatchers.IO) {
                initControls()
            }
        }
        //initControls()
    }
}