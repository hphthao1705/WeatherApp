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
import com.example.weatherapp.view.fragment.FragmentSearch
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        listData = loadData()

        searchCity()

    }
    fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.framelayout.id, fragment)
        fragmentTransaction.commit()
    }
    private fun loadData():ArrayList<Data>
    {
        var list:ArrayList<Data> = ArrayList()
        lifecycleScope.launch {
            var i = viewModel.loadCities()
            viewModel._liveData.observe(this@MainActivity)
            {
                list = it as ArrayList<Data>
//                if(i == "")
//                {
//
//                    //replaceFragment(FragmentCity(it))
//                    //replaceFragment(FragmentFavouriteCity())
//                    for (i in it)
//                    {
//                        list.add(i)
//                    }
//                }
                binding.btnListcity.setOnClickListener{
                    if(list.isNotEmpty())
                    {
                        replaceFragment(FragmentCity(list))
                    }
                    else
                    {
                        replaceFragment(FragmentEmptyStateCity())
                    }
                }
//                else
//                {
//                    replaceFragment(FragmentEmptyStateCity())
//                }
            }
        }

        viewModelSearch.getAllNote().observe(this){
            if(!it.isEmpty())
            {
                replaceFragment(FragmentFavouriteCity())
            }
            else{
                replaceFragment(FragmentEmptyFavouriteCity())

            }
        }
        return list
    }
    fun searchCity():String
    {
        var textSearch:String = ""
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