package com.example.weatherapp.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.utils.AppUtils
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.CityAdapter
import com.example.weatherapp.viewmodel.CityViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class CityFragment : Fragment() {

    private lateinit var binding: FragmentCityBinding
    private var adapter: CityAdapter = CityAdapter(emptyList())
    private lateinit var viewModel: CityViewModel
    private lateinit var mainActivity: MainActivity

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
        viewModel = getViewModel()
        mainActivity = activity as MainActivity

        viewModel.checkListCityData()

        viewModel.errorVisibility.observe(viewLifecycleOwner) {
            if(it == View.GONE) {
                binding.recyclerviewCity.layoutManager = GridLayoutManager(view?.context, 2)
                adapter = CityAdapter(AppUtils.getListCity())
                binding.recyclerviewCity.adapter = adapter
                adapter.setOnClickListener(object : CityAdapter.OnClickListener {
                    override fun onClick(city: Data) {
                        mainActivity.replaceFragment(DisplayWeatherFragment.newInstance(cityName = city.city))
                    }
                })
            }
            setVisibility(it)
        }
//        saveCurrentState()
    }

    fun setVisibility(visibility: Int) {
        binding.empty1.visibility = visibility
        binding.empty2.visibility = visibility
        binding.empty3.visibility = visibility
        if(visibility == View.GONE) {
            binding.layoutCity.visibility = View.VISIBLE
        } else {
            binding.layoutCity.visibility = View.GONE
        }
    }

//    private fun saveCurrentState() {
//        //countingIdlingResource.increment()
//        val activity: MainActivity? = activity as MainActivity
//
//        val sharedPref = activity?.getSharedPreferences(
//            "currentState",
//            AppCompatActivity.MODE_PRIVATE
//        ) ?: return
//        with(sharedPref.edit()) {
//            putString("state", "Cities")
//            commit()
//            //countingIdlingResource.decrement()
//        }
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
}