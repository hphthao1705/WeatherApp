package com.example.weatherapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.CityAdapter

class FragmentCity(private var listData: List<Data>, private var listRoom: List<Search>) : Fragment() {
    private lateinit var binding: FragmentCityBinding
    private var adapter: CityAdapter = CityAdapter(emptyList())
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
        //countingIdlingResource.increment()
        saveCurrentState()
        loadData()
        //countingIdlingResource.decrement()
    }

    private fun loadData()
    {
        //countingIdlingResource.increment()
        val activity: MainActivity? = activity as MainActivity
        if(listData.isNotEmpty())
        {
            binding.recyclerviewCity.layoutManager = GridLayoutManager(view?.context,2)
            adapter = CityAdapter(listData)
            binding.recyclerviewCity.adapter = adapter
            adapter.setOnClickListener(object:CityAdapter.OnClickListener{
                override fun onClick(city: Data) {
                    activity?.replaceFragment(FragmentHome())
                }
            })
            setVisibility(true)
        }
        else
        {
            setVisibility(false)

        }
        binding.loading.visibility = View.GONE
        //countingIdlingResource.decrement()
    }
    fun setVisibility(bool:Boolean)
    {
        //countingIdlingResource.increment()
        if(bool)
        {
            binding.layoutCity.visibility = View.VISIBLE
            binding.empty1.visibility = View.GONE
            binding.empty2.visibility = View.GONE
            binding.empty3.visibility = View.GONE
        }
        else
        {
            binding.layoutCity.visibility = View.GONE
            binding.empty1.visibility = View.VISIBLE
            binding.empty2.visibility = View.VISIBLE
            binding.empty3.visibility = View.VISIBLE
        }
        //countingIdlingResource.decrement()
    }
    private fun saveCurrentState()
    {
        //countingIdlingResource.increment()
        val activity: MainActivity? = activity as MainActivity

        val sharedPref = activity?.getSharedPreferences("currentState",
            AppCompatActivity.MODE_PRIVATE
        ) ?: return
        with (sharedPref.edit()) {
            putString("state", "Cities")
            commit()
            //countingIdlingResource.decrement()
        }
    }
}