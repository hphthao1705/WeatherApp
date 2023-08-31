package com.example.weatherapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.view.fragment.FragmentEmptyStateCity
import com.example.weatherapp.view.fragment.FragmentCity
import com.example.weatherapp.view.fragment.FragmentSearch
import com.example.weatherapp.viewmodel.CityViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val viewModel by viewModels<CityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        replaceFragment(FragmentCity())
        binding.btnListcity.setOnClickListener{
            loadCities()
        }
        searchCity()

    }
    fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.framelayout.id, fragment)
        fragmentTransaction.commit()
    }

    private fun loadCities()
    {
        lifecycleScope.launch {
            if(viewModel.loadCities() == "")
            {
                replaceFragment(FragmentCity())
            }
            else
            {
                replaceFragment(FragmentEmptyStateCity())
            }
        }
    }
    fun searchCity():String
    {
        var textSearch:String = ""
        binding.txtSearch.doAfterTextChanged {
            textSearch = it.toString()
            lifecycleScope.launch {
                viewModel.loadCities()
                viewModel._liveData.observe(this@MainActivity)
                {
                    replaceFragment(FragmentSearch(textSearch, it))
                }
            }
        }
//        binding.txtSearch.doOnTextChanged { text, start, before, count ->
//
//        }
        return textSearch
    }
}