package com.example.weatherapp.view.displayWeather

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.local.Search
import com.example.weatherapp.databinding.FragmentDisplayWeatherBinding
import com.example.weatherapp.utils.WeatherUtils
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.WeatherAdapter
import com.example.weatherapp.view.city.CityFragment
import com.example.weatherapp.view.city.CityViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class DisplayWeatherFragment : Fragment() {
    companion object {
        fun newInstance(
            cityName: String? = null,
            image: String? = null
        ): DisplayWeatherFragment {
            val fragment = DisplayWeatherFragment()
            val bundle = Bundle()
            bundle.putString("cityName", cityName)
            bundle.putString("image", image)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var binding: FragmentDisplayWeatherBinding
    private lateinit var viewModel: DisplayWeatherViewModel
    private val searchViewModel: CityViewModel by lazy {
        ViewModelProvider(this, CityViewModel.ViewModelFactory(this.requireActivity().application))[CityViewModel::class.java]
    }
    private var adapter: WeatherAdapter = WeatherAdapter(emptyList())
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
        val image = requireArguments().getString("image")
        cityName?.let {
            image?.let {
                addCityToRoom(cityName = cityName, image = image)
            }
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

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            mainActivity.showOrHideLoader(it)
        }

        binding.btnListcity.setOnClickListener {
            mainActivity.replaceFragment(CityFragment())
        }

        mainActivity.saveCurrentState(cityName = cityName.orEmpty())
    }

    private fun addCityToRoom(cityName: String, image: String) {
        var p = Search(name = cityName, image = image)
        searchViewModel.addNewCity(p)
    }

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
        try {
            binding.errorIcon.visibility = visible
            binding.errorContent.visibility = visible
            binding.errorTitle.visibility = visible
            if (visible == View.VISIBLE) {
                binding.cvCity.visibility = View.GONE
            } else {
                binding.cvCity.visibility = View.VISIBLE
            }

            binding.errorTitle.text = viewModel.errorTitle.value
            binding.errorContent.text = viewModel.errorContent.value
        } catch (ex: Exception) {

        } finally {
            viewModel.loadingVisibility.postValue(View.GONE)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
}