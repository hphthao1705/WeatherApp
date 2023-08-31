package com.example.weatherapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.CityAdapter
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
class FragmentCity() : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private val viewModel by activityViewModels<CityViewModel>()
    private var adapter: CityAdapter = CityAdapter(emptyList())
    private val viewModelWeather by activityViewModels<WeatherViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, com.example.weatherapp.R.layout.fragment_city, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData()
    {
        val activity: MainActivity? = activity as MainActivity
        lifecycleScope.launch {
            viewModel.loadCities()
            viewModel._liveData.observe(viewLifecycleOwner)
            {
                binding.recyclerviewCity.layoutManager = GridLayoutManager(view?.context,2)
                adapter = CityAdapter(it)
                binding.recyclerviewCity.adapter =  adapter

                adapter.setOnClickListener(object:CityAdapter.OnClickListener{
                    override fun onClick(city: Data) {
                        //Toast.makeText(context, city.city, Toast.LENGTH_SHORT).show()
                        lifecycleScope.launch {
                            Toast.makeText(context,viewModelWeather.loadWeather(city.city), Toast.LENGTH_SHORT).show()
                            if(viewModelWeather.loadWeather(city.city) == "")
                            {
                                activity?.replaceFragment(FragmentHome(city.city))
                            }
                            else
                            {
                                activity?.replaceFragment(FragmentEmptyStateWeather())
                            }
                        }

                    }
                })
            }
        }
    }
}