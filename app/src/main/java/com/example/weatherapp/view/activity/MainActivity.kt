package com.example.weatherapp.view.activity

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.view.city.CityFragment
import com.example.weatherapp.view.displayWeather.DisplayWeatherFragment
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    //    private val viewModelSearch: SearchViewModel by lazy {
//        ViewModelProvider(this@MainActivity, SearchViewModel.ViewModelFactory(this.application))[SearchViewModel::class.java]
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel.doneAPI.observe(this) {
            val cityName = getCurrentState()
            if(cityName.isNullOrBlank()) {
                replaceFragment(CityFragment())
            } else {
                replaceFragment(DisplayWeatherFragment.newInstance(cityName = cityName))
            }
        }

        viewModel.loadingVisibility.observe(this) {
            binding.loading.visibility = it
        }

        binding.viewModel = viewModel
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
    private fun getCurrentState(): String {
        val sharedPreferences = getSharedPreferences("currentState", MODE_PRIVATE)
        return sharedPreferences.getString("city", "")!!
    }

    fun saveCurrentState(cityName: String) {
        val sharedPref = getSharedPreferences("currentState", MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("city", cityName)
            commit()
        }
    }
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
        } else {
//            showOrHideLoader(View.GONE)
            replaceFragment(DisplayWeatherFragment.newInstance(cityName = ""))
        }
    }

    fun openDialog(className: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(getDrawable((R.drawable.customsearchtext)))
        dialog.setCancelable(false)
        val tvTitle: AppCompatTextView = dialog.findViewById(R.id.tvTitle)
        val tvDescription: AppCompatTextView = dialog.findViewById(R.id.tvDescription)
        val btnCancel: AppCompatButton = dialog.findViewById(R.id.btnCancel)
        val btnSubmit: AppCompatButton = dialog.findViewById(R.id.btnSubmit)

        tvTitle.text = viewModel.title.value
        tvDescription.text = viewModel.description.value
        btnCancel.text = viewModel.cancelText.value
        btnSubmit.text = viewModel.okText.value
        dialog.show()

        btnCancel.setOnClickListener { dialog.dismiss() }
    }

}