package com.example.weatherapp.view.fragment

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.databinding.FragmentDisplayWeatherBinding
import com.example.weatherapp.utils.WeatherUtils
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.WeatherAdapter
import com.example.weatherapp.viewmodel.DisplayWeatherViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class DisplayWeatherFragment : Fragment() {
    companion object {
        fun newInstance(
            cityName: String? = null,
        ): DisplayWeatherFragment {
            val fragment = DisplayWeatherFragment()
            val bundle = Bundle()
            bundle.putString("cityName", cityName)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentDisplayWeatherBinding
    private lateinit var viewModel: DisplayWeatherViewModel

    //private val viewModel:WeatherViewModel by inject()
    //private val viewModel by activityViewModels<WeatherViewModel>()
    private var adapter: WeatherAdapter = WeatherAdapter(emptyList())

    //    private val roomData by activityViewModels<SearchViewModel>()
//    private lateinit var viewModelSearch: SearchViewModel
    private var listRoom: ArrayList<Search> = ArrayList()
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_display_weather, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModel
        viewModel = getViewModel()

        val cityName = requireArguments().getString("cityName")
        cityName?.let {
            viewModel.loadWeather(city = it)
        } ?: let {
            viewModel.errorVisibility.value = View.VISIBLE
        }

        viewModel.errorVisibility.observe(viewLifecycleOwner) {
            displayWeather(it)
        }

        viewModel.weather.observe(viewLifecycleOwner) {
            binding.cityName.text = it.city
            binding.condition.text = it.condition
            binding.tempurate.text = it.tempurature

            //fix loi hinh
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            lifecycleScope.launch {
                binding.weatherIcon.setImageBitmap(WeatherUtils.getBitmapFromURL("https:${it.image}"))
            }

            binding.rcWeather.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL, false
            )
            it.properties?.let { it1 -> adapter.setData(it1) }
            binding.rcWeather.adapter = adapter
        }
        binding.btnListcity.setOnClickListener {

        }

        saveCurrentState(cityName = cityName.orEmpty())


//        viewModelSearch = getViewModel()
    }

    private fun saveCurrentState(cityName: String) {
        val activity: MainActivity? = activity as MainActivity

        val sharedPref = activity?.getSharedPreferences("currentState", MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("city", cityName)
            commit()
        }

        activity.showOrHideLoader(View.GONE)
    }

//    private fun addCityToRoom(cityName: String) {
//        var p = Search(cityName)
//        roomData.addNewCity(p)
//    }

//    private fun loadDataFromRoom() {
//        viewModelSearch.getAllNote().observe(this) {
//            listRoom = it as ArrayList<Search>
//        }
//    }

//    fun isError(result: Int) {
//        viewModel.errorVisibility.value = result
//        if (result == View.GONE) {
//            viewModel.weatherVisibility.value = View.VISIBLE
//        } else {
//            viewModel.weatherVisibility.value = View.GONE
//        }
//    }

    private fun displayWeather(visible: Int) {
        binding.errorIcon.visibility = visible
        binding.errorContent.visibility = visible
        binding.errorTitle.visibility = visible
        if (visible == View.VISIBLE) {
            binding.cvCity.visibility = View.GONE
        } else {
            binding.cvCity.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
}