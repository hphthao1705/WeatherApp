package com.example.weatherapp.view.fragment

//import com.example.weatherapp.view.search.SearchViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.weatherapp.R
import com.example.weatherapp.data.local.Search
import com.example.weatherapp.data.model.State
import com.example.weatherapp.data.model.city.Data
import com.example.weatherapp.databinding.FragmentNavigationBottomBinding
import com.example.weatherapp.view.city.CityFragment
import com.example.weatherapp.view.city.CityViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class FragmentNavigationBottom(
    val list_city: ArrayList<Data>,
    val list_room: ArrayList<Search>,
    val state: State
) : Fragment() {
    private lateinit var binding: FragmentNavigationBottomBinding
//    private lateinit var searchTextViewModel: SearchText
    private lateinit var viewModel: CityViewModel
//    private lateinit var viewModelSearch: SearchViewModel

//    private val viewModelSearch: SearchViewModel by lazy {
//        ViewModelProvider(viewLifecycleOwner, SearchViewModel.ViewModelFactory(activity.application))[SearchViewModel::class.java]
//    }

    //    private val onNavigationItemSelected = BottomNavigationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_navigation_bottom, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        replaceFragment(FragmentFavouriteCity(list_room))

//        searchTextViewModel = getViewModel()
        viewModel = getViewModel()
//        viewModelSearch = getViewModel()
//
//        clickOnNavigation()

        backToScreenBefore()
    }

//    private fun clickOnNavigation() {
//        binding.bottomNavigation.selectedItemId = R.id.city
//        binding.bottomNavigation.setOnNavigationItemSelectedListener {
//            when (it.itemId) {
//                //R.id.home -> replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
//                R.id.home -> replaceFragment(FragmentFavouriteCity(list_room))
//                R.id.city -> replaceFragment(FragmentCity(list_city))
//            }
//            true
//        }
//    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager? = fragmentManager
        fragmentManager?.beginTransaction()?.replace(binding.frameLayout2.id, fragment)
            ?.addToBackStack(null)?.commit()
    }


//    private fun loadCities() {
//        //countingIdlingResource.increment()
//        lifecycleScope.launch {
//            viewModel._liveData.observe(viewLifecycleOwner) {
//                listData = it as ArrayList<Data>
//            }
//            viewModel._liveDataAPI?.observe(viewLifecycleOwner) {
//            }
//            //countingIdlingResource.decrement()
//        }
//
//        binding.bottomNavigation.selectedItemId = R.id.home
//
//        //replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
//        replaceFragment(FragmentFavouriteCity())
//    }

//    private fun loadDataFromRoom() {
//        //countingIdlingResource.increment()
//        viewModelSearch.getAllNote().observe(viewLifecycleOwner) {
//            list_room = it as ArrayList<Search>
//            //countingIdlingResource.decrement()
//        }
//    }

    private fun backToScreenBefore() {
//        val activity: MainActivity? = activity as MainActivity
        when (state.state) {
//            "Home" -> replaceFragment(FragmentFavouriteCity(list_room))
            "Cities" -> replaceFragment(CityFragment()) //replaceFragment(FragmentFavouriteCity(listRoom.isNotEmpty(), listRoom))
//            "City" -> activity?.replaceFragment(FragmentHome(state.city, list_room))
//            "Location" -> replaceFragment(FragmentCity(listData, listRoom))
//            else -> replaceFragment(FragmentFavouriteCity(list_room))
        }
    }
}