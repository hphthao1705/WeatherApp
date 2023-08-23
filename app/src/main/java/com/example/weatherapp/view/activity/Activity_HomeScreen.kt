package com.example.weatherapp.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager

import com.example.weatherapp.R.layout.activity_home_screen
import com.example.weatherapp.databinding.ActivityHomeScreenBinding
import com.example.weatherapp.view.adapter.CityAdapter
import com.example.weatherapp.viewmodel.CityViewModel
import kotlinx.coroutines.launch

class Activity_HomeScreen : AppCompatActivity() {
    private lateinit var binding:ActivityHomeScreenBinding
    private val viewModel by viewModels<CityViewModel>()
    private var adapter: CityAdapter = CityAdapter(emptyList())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, activity_home_screen)

        lifecycleScope.launch {
            viewModel.loadCities()
            viewModel._liveData.observe(this@Activity_HomeScreen)
            {
                binding.recyclerviewCity.layoutManager = GridLayoutManager(this@Activity_HomeScreen,2)
                adapter = CityAdapter(it)
                binding.recyclerviewCity.adapter =  adapter
            }
        }

        binding.searchbar.setOnSearchClickListener {
            val intent: Intent = Intent(this, Activity_SearchScreen::class.java)
            finish()
            startActivity(intent)
        }
    }
}