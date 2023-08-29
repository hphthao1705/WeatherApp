package com.example.weatherapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.view.activity.ActivityEmptyState
import com.example.weatherapp.view.activity.ActivityCityScreen
import com.example.weatherapp.view.adapter.CityAdapter
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch


class FragmentHome : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val viewModel by activityViewModels<CityViewModel>()
    private var adapter: CityAdapter = CityAdapter(emptyList())
    private val viewModelWeather by activityViewModels<WeatherViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, com.example.weatherapp.R.layout.fragment_home, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData()
    {
        lifecycleScope.launch {
            viewModel.loadCities()
            viewModel._liveData.observe(viewLifecycleOwner)
            {
                binding.recyclerviewCity.layoutManager = GridLayoutManager(view?.context,2)
                adapter = CityAdapter(it)
                binding.recyclerviewCity.adapter =  adapter

                adapter.setOnClickListener(object:CityAdapter.OnClickListener{
                    override fun onClick(city: Data) {
                        lifecycleScope.launch {
                            if(viewModelWeather.loadWeather(city.city) == "")
                            {
                                val intent = Intent(activity, ActivityCityScreen::class.java)
                                intent.putExtra("cityname", city.city)
                                activity?.finish()
                                startActivity(intent)
                            }
                            else
                            {
                                val intent2 = Intent(activity, ActivityEmptyState::class.java)
                                activity?.finish()
                                startActivity(intent2)
                            }
                        }

                    }
                })
            }
        }
    }
}