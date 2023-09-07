package com.example.weatherapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        replaceFragment(FragmentLoading())
        loadCities()
    }
    fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.framelayout.id, fragment)
        fragmentTransaction.commit()
    }
    private fun loadCities() = runBlocking{
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
        initControls()
    }
    suspend fun initControls()
    {
        delay(5000)
        binding.btnListcity.setOnClickListener{
            if(listData.isNotEmpty())
            {
                replaceFragment(FragmentCity(listData))
            }
            else
            {
                replaceFragment(FragmentEmptyStateCity())
            }
        }
        searchCity()
        viewModelSearch.getAllNote().observe(this@MainActivity){
            if(it.isNotEmpty())
            {
                replaceFragment(FragmentFavouriteCity())
            }
            else{
                replaceFragment(FragmentEmptyFavouriteCity())
            }
        }
//        binding.btnListfavouritecity.setOnClickListener{
//
//        }
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
//        binding.txtSearch.doOnTextChanged { text, start, before, count ->
//
//        }
        return textSearch
    }
}