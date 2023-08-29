package com.example.weatherapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.view.fragment.FragmentEmptyState
import com.example.weatherapp.view.fragment.FragmentHome
import com.example.weatherapp.view.fragment.FragmentSearch
import com.example.weatherapp.viewmodel.CityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var toolbar: Toolbar
    private val viewModel by viewModels<CityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        toolbar = findViewById(R.id.searchbar)
        setSupportActionBar(toolbar)

        loadWhenAppOpen()
        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId)
            {
                R.id.home -> {
                    lifecycleScope.launch {
                        if(viewModel.loadCities() == "")
                        {
                            replaceFragment(FragmentHome())
                        }
                        else
                        {
                            replaceFragment(FragmentEmptyState())
                        }
                    }
                }
                R.id.search -> replaceFragment(FragmentSearch())
                else -> replaceFragment(FragmentHome())
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.header, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.search_tool -> {
                actionBar?.hide()
                replaceFragment(FragmentSearch())
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun replaceFragment(fragment: Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.framelayout.id, fragment)
        fragmentTransaction.commit()
    }

    private fun loadWhenAppOpen()
    {
        lifecycleScope.launch {
            if(viewModel.loadCities() == "")
            {
                replaceFragment(FragmentHome())
            }
            else
            {
                replaceFragment(FragmentEmptyState())
            }
        }
    }
}