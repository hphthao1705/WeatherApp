package com.example.weatherapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.SearchAdapter
import com.example.weatherapp.viewmodel.SearchViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel

class FragmentSearch(var text:String, private var listData: List<Data>, private var listRoom: List<Search>) : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModelSearch: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.ViewModelFactory(requireActivity().application)
        )[SearchViewModel::class.java]
    }
    private var adapter: SearchAdapter = SearchAdapter(emptyList())
    private val viewModelWeather by activityViewModels<WeatherViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewSearch.setHasFixedSize(true)
        binding.recyclerviewSearch.layoutManager = LinearLayoutManager(context)
        adapter = SearchAdapter(listData)
        binding.recyclerviewSearch.adapter = adapter
        searchCity(text)
        clickOnCity()
    }
    private fun clickOnCity()
    {
        val activity: MainActivity? = activity as MainActivity
        adapter.setOnClickListener(object :SearchAdapter.OnClickListener{
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
    private fun searchCity(text: String)
    {
        var filterList = emptyList<Data>()
        if(text!=null)
        {
           filterList = listData.filter {
               it.city.lowercase().contains(text)
           }
        }
        Toast.makeText(context,filterList.size.toString(),Toast.LENGTH_SHORT).show()
        adapter = SearchAdapter(filterList)
        binding.recyclerviewSearch.adapter = adapter
       //adapter.notifyDataSetChanged()
    }
}