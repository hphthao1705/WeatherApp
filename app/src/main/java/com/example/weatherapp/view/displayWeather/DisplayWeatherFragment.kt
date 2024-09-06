package com.example.weatherapp.view.displayWeather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.local.Search
import com.example.weatherapp.databinding.FragmentDisplayWeatherBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.city.CityFragment
import com.example.weatherapp.view.city.CityViewModel
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
//    private var adapter: WeatherAdapter = WeatherAdapter(emptyList())
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
        binding.lifecycleOwner = this

        mainActivity.showOrHideLoader(View.VISIBLE)

        val cityName = requireArguments().getString("cityName")
        val image = requireArguments().getString("image")
        cityName?.let {
            image?.let {
                addCityToRoom(cityName = cityName, image = image)
            }
            viewModel.loadWeather(cityName = it)
        } ?: let {
            viewModel.errorVisibility.value = View.VISIBLE
        }

        viewModel.errorVisibility.observe(viewLifecycleOwner) {
            if(it == View.VISIBLE) {
                viewModel.dataVisibility.value = View.GONE
                viewModel.loadingVisibility.value = View.GONE
            }
        }

        viewModel.isSuccessful.observe(viewLifecycleOwner) {
            //fix loi hinh neu bi loi
//            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//            StrictMode.setThreadPolicy(policy)
            setUpRecyclerView()
            viewModel.loadingVisibility.value = View.GONE
        }

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            mainActivity.showOrHideLoader(it)
        }

        searchViewModel.isDoneDelete.observe(viewLifecycleOwner) {
            var p = Search(name = cityName, image = image)
            searchViewModel.addNewCity(p)
        }

        binding.btnListcity.setOnClickListener {
            mainActivity.replaceFragment(CityFragment())
        }

        binding.viewModel = viewModel
        mainActivity.saveCurrentState(cityName = cityName.orEmpty())
    }

    private fun addCityToRoom(cityName: String, image: String) {
        var p = Search(name = cityName, image = image)
        searchViewModel.checkExistence(p)
    }

    private fun setUpRecyclerView() {
        binding.rcWeather.setHasFixedSize(true)
        binding.rcWeather.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onResume() {
        super.onResume()
    }
}