package com.example.weatherapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.SearchAdapter
import com.example.weatherapp.viewmodel.SearchText

class FragmentSearch(
    var text: String,
    private var listData: List<Data>,
    private var listRoom: List<Search>
) : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var adapter: SearchAdapter = SearchAdapter(emptyList())
    private val viewModel: SearchText by activityViewModels<SearchText>()
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

    private fun clickOnCity() {
        val activity: MainActivity? = activity as MainActivity
        adapter.setOnClickListener(object : SearchAdapter.OnClickListener {
            override fun onClick(city: Data) {
                viewModel.searchDone()
                activity?.replaceFragment(DisplayWeatherFragment())
            }
        })
    }

    private fun searchCity(text: String) {
        var filterList = emptyList<Data>()
        if (text != null) {
            filterList = listData.filter {
                it.city.startsWith(text, true)
            }
            if (filterList.size == 0) {
//                Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show()
                setVisibility(false)
            } else {
                setVisibility(true)
            }
//            binding.loading.visibility = View.GONE
        }
        //Toast.makeText(context,filterList.size.toString(), .LENGTH_SHORT).show()
        adapter = SearchAdapter(filterList)
        binding.recyclerviewSearch.adapter = adapter
        //adapter.notifyDataSetChanged()
    }

    private fun setVisibility(bool: Boolean) {
        if (bool) {
            binding.recyclerviewSearch.visibility = View.VISIBLE
            binding.empty1.visibility = View.GONE
            binding.empty2.visibility = View.GONE
            binding.empty3.visibility = View.GONE
        } else {
            binding.recyclerviewSearch.visibility = View.GONE
            binding.empty1.visibility = View.VISIBLE
            binding.empty2.visibility = View.VISIBLE
            binding.empty3.visibility = View.VISIBLE
        }
    }
}