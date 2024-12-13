package com.example.weatherapp.view.city

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.CheckResult
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.data.model.CityData
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.utils.AppUtils
import com.example.weatherapp.utils.Prefs
import com.example.weatherapp.utils.location.LocationProvider
import com.example.weatherapp.utils.location.LocationUtils
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.city.adapter.CityAdapter
import com.example.weatherapp.view.city.adapter.SearchAdapter
import com.example.weatherapp.view.city.uiViewModel.CityUIViewModel
import com.example.weatherapp.view.displayWeather.DisplayWeatherFragment
import com.google.android.gms.common.internal.Preconditions.checkMainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class CityFragment : Fragment() {

    private lateinit var binding: FragmentCityBinding
    private val viewModel: CityViewModel by lazy {
        ViewModelProvider(
            this,
            CityViewModel.ViewModelFactory(this.requireActivity().application)
        )[CityViewModel::class.java]
    }
    private lateinit var mainActivity: MainActivity
    private lateinit var locationProvider: LocationProvider
    private var dbCity: CityData? = null
    private val coroutineScopeIO = CoroutineScope(Dispatchers.IO)

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
        mainActivity = activity as MainActivity
        binding.lifecycleOwner = this

        dbCity = Prefs.getCityData()
        locationProvider = LocationProvider(
            mainActivity,
            this, LocationUtils.getCallBack(this::onSuccess)
        )

        viewModel.getAll()

        AppUtils.cityListData.observe(viewLifecycleOwner) {
//            mainActivity.showOrHideLoader(View.GONE)
        }

        viewModel.isDone.observe(viewLifecycleOwner) {
            if (viewModel.favoriteCities.isNotEmpty()) {
                setUpFavoriteRecyclerView()
                viewModel.favoriteAdapter.setDataList(viewModel.favoriteCities.asSequence().map {
                    CityUIViewModel.from(it)
                }.toList())
                viewModel.favoriteAdapter.setOnClickListener(object : CityAdapter.OnClickListener {
                    override fun onClick(city: CityUIViewModel) {
                        mainActivity.replaceFragment(
                            DisplayWeatherFragment.newInstance(
                                cityName = city.city,
                                image = city.image
                            )
                        )
                    }
                })
                viewModel.favoriteVisibility.value = View.VISIBLE
            } else {
                viewModel.errorVisibility.value = View.GONE
            }
        }

        if(dbCity?.city.isNullOrBlank() && AppUtils.isCheckNewLocation) {
            checkLocation(Prefs.getCityData().city)
        }

        viewModel.errorVisibility.observe(viewLifecycleOwner) {
            if(it == View.VISIBLE) {
                viewModel.searchVisibility.value = View.GONE
                viewModel.favoriteVisibility.value = View.GONE
                setErrorMessageDisplay()
            }
            viewModel.loadingVisibility.value = View.GONE
        }

        viewModel.searchVisibility.observe(viewLifecycleOwner) {
            if(it == View.VISIBLE) {
                viewModel.favoriteVisibility.value = View.GONE
                viewModel.errorVisibility.value = View.GONE
            }
        }


        viewModel.favoriteVisibility.observe(viewLifecycleOwner) {
            if(it == View.VISIBLE) {
                if (viewModel.favoriteCities.isNotEmpty()) {
                    setUpFavoriteRecyclerView()
                    viewModel.favoriteAdapter.setDataList(viewModel.favoriteCities.asSequence().map {
                        CityUIViewModel.from(it)
                    }.toList())
                    viewModel.favoriteAdapter.setOnClickListener(object : CityAdapter.OnClickListener {
                        override fun onClick(city: CityUIViewModel) {
                            mainActivity.replaceFragment(
                                DisplayWeatherFragment.newInstance(
                                    cityName = city.city,
                                    image = city.image
                                )
                            )
                        }
                    })
                    viewModel.searchVisibility.value = View.GONE
                    viewModel.errorVisibility.value = View.GONE
                }
                else {
                    viewModel.errorVisibility.value = View.VISIBLE
                }
            }
        }

        viewModel.loadingVisibility.observe(viewLifecycleOwner) {
            mainActivity.showOrHideLoader(it)
        }

        binding.txtSearch.textChanges().debounce(500).onEach { searchCity(it.toString()) }
            .launchIn(lifecycleScope)

        Prefs.saveCurrentState("")

        binding.viewModel = viewModel
    }

    private fun searchCity(text: String) {
        var filterList = emptyList<CityUIViewModel>()

        if (!text.isNullOrBlank()) {
            viewModel.isSearch.value = true
            filterList = AppUtils.getListCity()?.filter {
                it.city.startsWith(text, true)
            }?.take(8).orEmpty()

            setUpSearchRecyclerView()
            viewModel.searchAdapter.setDataList(filterList)
            viewModel.searchAdapter.setOnClickListener(object : SearchAdapter.OnClickListener {
                override fun onClick(city: CityUIViewModel) {
                    mainActivity.replaceFragment(
                        DisplayWeatherFragment.newInstance(
                            cityName = city.city,
                            image = city.image
                        )
                    )
                }
            })

            filterList?.let {
                if (it.isNotEmpty()) {
                    viewModel.searchVisibility.value = View.VISIBLE
                } else {
                    viewModel.errorVisibility.value = View.VISIBLE
                }
            } ?: run {
                viewModel.errorVisibility.value = View.VISIBLE
            }
        } else {
            //If user is not search anymore, back to favorite list
            viewModel.isSearch.value = false
            viewModel.favoriteVisibility.value = View.VISIBLE
        }
    }

    private fun setUpFavoriteRecyclerView() {
        binding.recyclerviewCity.setHasFixedSize(true)
        binding.recyclerviewCity.layoutManager = LinearLayoutManager(view?.context)
    }

    private fun setUpSearchRecyclerView() {
        binding.recyclerviewSearch.setHasFixedSize(true)
        binding.recyclerviewSearch.layoutManager = LinearLayoutManager(context)
    }

    private fun setErrorMessageDisplay() {
        if (viewModel.isSearch.value == true) {
            viewModel.errorTitle.value = viewModel.emptyTitleSearch.value
            viewModel.errorContent.value = viewModel.emptyContentSearch.value
        } else {
            viewModel.errorTitle.value = viewModel.emptyTitleFavorite.value
            viewModel.errorContent.value = viewModel.emptyContentFavorite.value
        }
    }

    private fun checkLocation(dbCity: String?) {
        if (dbCity != null) {
            locationProvider.onLocation()
        }
    }

    fun onSuccess(location: Location?) {
        if (location != null) {
            val addresses: List<Address>?
            val geoCoder = Geocoder(mainActivity)
            locationProvider.removeGetLocation()
            try {
                if (Geocoder.isPresent()) {
                    addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)

                    if (!addresses.isNullOrEmpty()) {
                        var city: String? = addresses[0].locality
                        if (city.isNullOrEmpty()) {
                            city = addresses[0].adminArea
                        }

                        if (!city.isNullOrEmpty() && !dbCity?.city.isNullOrEmpty() && city != dbCity?.city && AppUtils.isCheckNewLocation) {
                            val distance = calculateDistance(location) //km
                            if (distance > 100) {
                                Log.d("Thao Ho", "distance > 100")
//                                mainActivity.showDialogNewLocation(
//                                    viewModel.titleDialog.value ?: "",
//                                    viewModel.descriptionDialog.value ?: "",
//                                    viewModel.updateButton.value ?: "",
//                                    viewModel.keepButton.value ?: "",
//                                    location
//                                ) {
//                                    applyNewLocation(
//                                        city,
//                                        addresses[0].countryName,
//                                        location
//                                    )
//                                }
                            }
                            AppUtils.isCheckNewLocation = false
                            return
                        }
//                        if (!city.isNullOrEmpty() && city != viewModel.citySelected.value?.location || !city.isNullOrEmpty() && AppUtils.isChangeCity) {
////                            applyNewLocation(city, addresses[0].countryName ?: "New York", location)
//                        }
                    } else {
                        AppUtils.isCheckNewLocation = false
                    }
                }
            } catch (_: Exception) {

            }

        } else {
            locationProvider.onLocation()
            locationProvider.startLocationUpdates()
        }
    }

    private fun calculateDistance(location: Location): Float {
        val newLocation = Location("")
        newLocation.latitude = location.latitude
        newLocation.longitude = location.longitude

        val oldLocation = Location("")
        oldLocation.latitude = Prefs.latitude.toDoubleOrNull() ?: 0.0
        oldLocation.longitude = Prefs.longitude.toDoubleOrNull() ?: 0.0


        if (oldLocation.latitude == 0.0 || oldLocation.longitude == 0.0) {
            return 0f
        }

        return oldLocation.distanceTo(newLocation) / 1000
    }

//    private fun applyNewLocation(city: String, countryName: String?, location: Location) {
//        coroutineScopeIO.launch {
//            ExploredUtils.cacheCity(city, countryName, isLocate = true)
//
//            withContext(Dispatchers.Main) {
//                viewModel.getWeather(location.latitude, location.longitude)
//                Prefs.latitude = location.latitude.toString()
//                Prefs.longitude = location.longitude.toString()
//                AppUtils.isCheckNewLocation = false
//                AppUtils.isChangeCity = true
//                sharedViewModel.citySelected.value = ExploredUtils.getSelectedCity()
//            }
//        }
//    }


    @ExperimentalCoroutinesApi
    @CheckResult
    fun EditText.textChanges(): Flow<CharSequence?> {
        return callbackFlow {
            checkMainThread()

            val listener = doOnTextChanged { text, _, _, _ -> trySend(text) }
            awaitClose { removeTextChangedListener(listener) }
        }.onStart { emit(text) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
}