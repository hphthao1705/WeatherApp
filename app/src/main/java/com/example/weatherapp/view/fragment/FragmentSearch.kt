package com.example.weatherapp.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.SearchAdapter
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

class FragmentSearch(var text:String, private var listData: List<Data>) : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by activityViewModels<CityViewModel>()
    private var adapter: SearchAdapter = SearchAdapter(emptyList())
    private val viewModelWeather by activityViewModels<WeatherViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        val view: View = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerviewSearch.layoutManager = LinearLayoutManager(context)
        adapter = SearchAdapter(listData)
        binding.recyclerviewSearch.adapter = adapter
        searchCity(text)
        clickOnCity()
    }
    fun clickOnCity()
    {
        val activity: MainActivity? = activity as MainActivity
        adapter.setOnClickListener(object :SearchAdapter.OnClickListener{
            override fun onClick(city: Data) {
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
        adapter.notifyDataSetChanged()
    }
}