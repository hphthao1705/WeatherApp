package com.example.weatherapp.view.fragment

import android.content.Context
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
import com.example.weatherapp.databinding.FragmentFavouriteCityBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.FavouriteCityAdapter
import com.example.weatherapp.viewmodel.SearchViewModel

class FragmentFavouriteCity(val list_room: ArrayList<Search>) : Fragment() {
    private lateinit var binding:FragmentFavouriteCityBinding
    private val viewModel: SearchViewModel by activityViewModels()
    private var adapter:FavouriteCityAdapter = FavouriteCityAdapter(emptyList())
//    private lateinit var viewModelSearch:SearchViewModel
//    private var listRoom:ArrayList<Search> = ArrayList()

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
        binding.loading.visibility = View.VISIBLE
//        viewModelSearch = getViewModel()
        //loadDataFromRoom()

        saveCurrentState()
        initControls()

        binding.loading.visibility = View.GONE
    }
    private fun initControls()
    {
        when(list_room.isNotEmpty())
        {
            true->{
                viewModel.getAllNote().observe(viewLifecycleOwner){
                    adapter = FavouriteCityAdapter(it)
                    binding.recyclerviewFavouritecity.setHasFixedSize(true)
                    binding.recyclerviewFavouritecity.layoutManager = LinearLayoutManager(context)
                    binding.recyclerviewFavouritecity.adapter =  adapter
                    //adapter.notifyDataSetChanged()

                    val activity: MainActivity? = activity as MainActivity
                    adapter.setOnClickListener(object: FavouriteCityAdapter.OnClickListener{
                        override fun onClick(city: Search) {
                            activity?.replaceFragment(FragmentHome())
                        }
                    })
                }

                binding.loading.visibility = View.GONE
                setVisibility(true)
            }
            else->{

                binding.loading.visibility = View.GONE
                setVisibility(false)
            }
        }
    }
    fun saveCurrentState()
    {
        //CountingIdlingResourceSingleton.increment()
        val activity: MainActivity? = activity as MainActivity

        val sharedPref = activity?.getSharedPreferences("currentState", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("state", "Home")
            commit()
        }
        //CountingIdlingResourceSingleton.decrement()
    }
    fun setVisibility(bool:Boolean)
    {
        //CountingIdlingResourceSingleton.increment()
        if(bool)
        {
            binding.recyclerviewFavouritecity.visibility = View.VISIBLE
            binding.empty1.visibility = View.GONE
            binding.empty2.visibility = View.GONE
            binding.empty3.visibility = View.GONE
        }
        else
        {
            binding.recyclerviewFavouritecity.visibility = View.GONE
            binding.empty1.visibility = View.VISIBLE
            binding.empty2.visibility = View.VISIBLE
            binding.empty3.visibility = View.VISIBLE
        }
        //CountingIdlingResourceSingleton.decrement()
    }

//    fun loadDataFromRoom()
//    {
//        //countingIdlingResource.increment()
//        viewModelSearch.getAllNote().observe(viewLifecycleOwner){
//            list_room = it as ArrayList<Search>
//            //countingIdlingResource.decrement()
//        }
//    }
}