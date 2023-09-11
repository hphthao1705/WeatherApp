package com.example.weatherapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.CityAdapter
import com.example.weatherapp.viewmodel.SearchViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
class FragmentCity(private var listData: List<Data>, private var listRoom:List<Search>) : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private var adapter: CityAdapter = CityAdapter(emptyList())
    private val viewModelWeather by activityViewModels<WeatherViewModel>()
    private val viewModelSearch: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.ViewModelFactory(requireActivity().application)
        )[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.example.weatherapp.R.layout.fragment_city,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData()
    {
        val activity: MainActivity? = activity as MainActivity
        binding.recyclerviewCity.layoutManager = GridLayoutManager(view?.context,2)
        adapter = CityAdapter(listData)
        binding.recyclerviewCity.adapter = adapter

        adapter.setOnClickListener(object:CityAdapter.OnClickListener{
            override fun onClick(city: Data) {
                viewModelWeather._liveDataAPI?.observe(viewLifecycleOwner, Observer {
                    activity?.replaceFragment(FragmentEmptyStateWeather())
                })
                val predicate: (Search) -> Boolean = {it.name == city.city}
                if(listRoom.any(predicate))
                {
                    viewModelSearch.deleteCity(city.city)
                }
                var p = Search(city.city)
                viewModelSearch.addNewCity(p)
                activity?.replaceFragment(FragmentHome(city.city))
            }
        })
    }
}