package com.example.weatherapp.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.view.fragment.DisplayWeatherFragment
import com.example.weatherapp.view.fragment.FragmentCity
import com.example.weatherapp.viewmodel.MainActivityViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var listData: ArrayList<Data> = ArrayList()

    //    private val viewModelSearch: SearchViewModel by lazy {
//        ViewModelProvider(this@MainActivity, SearchViewModel.ViewModelFactory(this.application))[SearchViewModel::class.java]
//    }
//    private lateinit var viewModelCity: CityViewModel

    //    private lateinit var searchTextViewModel: SearchText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModelCity = getViewModel()
        viewModel = getViewModel()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.doneAPI.observe(this) {
            replaceFragment(FragmentCity())
        }

        viewModel.loadingVisibility.observe(this) {
            binding.loading.visibility = it
        }
//        searchTextViewModel = getViewModel()

//        showOrHideLoader(View.VISIBLE)

//        loadCities()

//        viewModel.doneAPI.observe(this) {
//            if (it) {
////                showOrHideLoader(View.GONE)
//                checkSelectedCity()
//            }
//        }
//
//        hearEventSearchFinish()

//        searchTextViewModel = getViewModel()
//        //viewMongIdlingResource.increment()
//        replaceFdelSearch = getViewModel()
//        //countiragment(FragmentLoading())
//        loadCities()

//        //countingIdlingResource.decrement()
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.framelayout.id, fragment)
        fragmentTransaction.commit()
    }

    fun showOrHideLoader(result: Int) {
        viewModel.loadingVisibility.value = result
    }

    //    fun initControls() {
//        binding.btnListcity.setOnClickListener {
//            //replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
//            replaceFragment(FragmentNavigationBottom(listData, listRoom, getCurrentState()))
//        }
//        searchCity()
//        //loadScreenWhenAppStart()
//    }
//
//    //    fun loadScreenWhenAppStart()
////    {
////        if(listRoom.isNotEmpty())
////        {
////            //replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
////            replaceFragment(FragmentFavouriteCity(listRoom))
////        }
////        else
////        {
////            replaceFragment(FragmentCity(listData, listRoom))
////        }
////    }
//    fun searchCity(): String {
//        var textSearch = ""
//        binding.txtSearch.doAfterTextChanged {
//            //binding.txtSearch.setImeHintLocales(LocaleList(Locale("zh", "CN")))
//            textSearch = it.toString()
//            if (textSearch.equals("", true)) {
//                val state = getCurrentState()
//                when (state.state) {
//                    "Home" -> replaceFragment(FragmentFavouriteCity(listRoom))
//                    "Cities" -> replaceFragment(
//                        FragmentCity(
//                            listData,
//                            listRoom
//                        )
//                    ) //replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
//                    else -> replaceFragment(FragmentFavouriteCity(listRoom))
//                }
//            } else {
//                replaceFragment(FragmentSearch(textSearch, listData, listRoom))
//            }
//        }
//        return textSearch
//    }

    //
//    fun loadCities() {
//        viewModelCity._liveData.observe(this) {
//            listData = it as ArrayList<Data>
//            viewModel.doneAPI.value = true
////            initControls()
////            replaceFragment(FragmentNavigationBottom(listData, listRoom, getCurrentState()))
//        }
//        viewModelCity._liveDataAPI?.observe(this@MainActivity) {
////            initControls()
//        }
//    }
//
//    private fun getCurrentState(): State {
//        val sharedPreferences = getSharedPreferences("currentState", MODE_PRIVATE)
//        val s1 = sharedPreferences.getString("state", "null roi be oi :(((")!!
//        val s2 = sharedPreferences.getString("city", "null roi be oi :(((")!!
//        return State(s1, s2)
//    }
//
//    private fun hearEventSearchFinish() {
//        searchTextViewModel.LiveData?.observe(this) {
//            binding.txtSearch.text.clear()
//        }
//    }

    fun checkSelectedCity() {
        val sharedPreferences = getSharedPreferences("currentState", MODE_PRIVATE)
        val city = sharedPreferences.getString("city", "").orEmpty()
        if (!city.isNullOrBlank()) {
            replaceFragment(DisplayWeatherFragment.newInstance(cityName = city))
        }
        else {
//            showOrHideLoader(View.GONE)
            replaceFragment(DisplayWeatherFragment.newInstance(cityName = ""))
        }
    }

}