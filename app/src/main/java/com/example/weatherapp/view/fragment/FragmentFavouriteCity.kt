package com.example.weatherapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavouriteCityBinding
import com.example.weatherapp.view.adapter.FavouriteCityAdapter
import com.example.weatherapp.viewmodel.SearchViewModel

class FragmentFavouriteCity : Fragment() {
    private lateinit var binding:FragmentFavouriteCityBinding
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            SearchViewModel.ViewModelFactory(requireActivity().application)
        )[SearchViewModel::class.java]
    }
    private var adapter:FavouriteCityAdapter = FavouriteCityAdapter(emptyList())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_city, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControls()

    }
    private fun initControls()
    {
        viewModel.getAllNote().observe(viewLifecycleOwner){
            adapter = FavouriteCityAdapter(it)
            binding.recyclerviewFavouritecity.setHasFixedSize(true)
            binding.recyclerviewFavouritecity.layoutManager = LinearLayoutManager(context)
            binding.recyclerviewFavouritecity.adapter =  adapter
            //adapter.notifyDataSetChanged()
        }
    }
}