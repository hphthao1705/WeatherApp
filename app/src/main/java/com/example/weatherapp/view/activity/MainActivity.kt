package com.example.weatherapp.view.activity

import android.os.Bundle
import android.os.LocaleList
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
import com.example.weatherapp.view.fragment.FragmentNavigationBottom
import com.example.weatherapp.view.fragment.FragmentSearch
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.SearchText
import com.example.weatherapp.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel:CityViewModel
    private var listData:ArrayList<Data> = ArrayList()
//    private val viewModelSearch: SearchViewModel by lazy {
//        ViewModelProvider(this@MainActivity, SearchViewModel.ViewModelFactory(this.application))[SearchViewModel::class.java]
//    }
    private lateinit var viewModelSearch:SearchViewModel
    private lateinit var searchTextViewModel:SearchText
    private var listRoom:ArrayList<Search> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelSearch = getViewModel()
        searchTextViewModel = getViewModel()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        replaceFragment(FragmentLoading())

        viewModel = getViewModel()

        loadCities()
        loadDataFromRoom()

        hearEventSearchFinish()

//        searchTextViewModel = getViewModel()
//        //viewMongIdlingResource.increment()
//        replaceFdelSearch = getViewModel()
//        //countiragment(FragmentLoading())
//        loadCities()

//        //countingIdlingResource.decrement()
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
            //replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
            replaceFragment(FragmentNavigationBottom(listData, listRoom, getCurrentState()))
        }
        searchCity()
        //loadScreenWhenAppStart()
    }
//    fun loadScreenWhenAppStart()
//    {
//        if(listRoom.isNotEmpty())
//        {
//            //replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
//            replaceFragment(FragmentFavouriteCity(listRoom))
//        }
//        else
//        {
//            replaceFragment(FragmentCity(listData, listRoom))
//        }
//    }
    fun searchCity():String
    {
        var textSearch = ""
        binding.txtSearch.doAfterTextChanged {
            //binding.txtSearch.setImeHintLocales(LocaleList(Locale("zh", "CN")))
            textSearch = it.toString()
            if(textSearch.equals("", true))
            {
                val state = getCurrentState()
                when(state.state)
                {
                    "Home" -> replaceFragment(FragmentFavouriteCity(listRoom))
                    "Cities" -> replaceFragment(FragmentCity(listData, listRoom)) //replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
                    else -> replaceFragment(FragmentFavouriteCity(listRoom))
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
                replaceFragment(FragmentNavigationBottom(listData, listRoom, getCurrentState()))
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
    private fun hearEventSearchFinish()
    {
        searchTextViewModel.LiveData?.observe(this){
            binding.txtSearch.text.clear()
        }
    }

}