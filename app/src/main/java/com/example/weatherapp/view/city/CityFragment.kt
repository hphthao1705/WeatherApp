package com.example.weatherapp.view.city

import android.content.Context
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
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.utils.AppUtils
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.CityAdapter
import com.example.weatherapp.view.adapter.SearchAdapter
import com.example.weatherapp.view.city.uiViewModel.CityUIViewModel
import com.example.weatherapp.view.displayWeather.DisplayWeatherFragment
import com.google.android.gms.common.internal.Preconditions.checkMainThread
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
    private var cityAdapter: CityAdapter = CityAdapter()
    private var searchAdapter: SearchAdapter = SearchAdapter()
    private val viewModel: CityViewModel by lazy {
        ViewModelProvider(this, CityViewModel.ViewModelFactory(this.requireActivity().application))[CityViewModel::class.java]
    }
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
        mainActivity = activity as MainActivity
        mainActivity.showOrHideLoader(View.VISIBLE)

        viewModel.getAll()

        AppUtils.cityListData.observe(viewLifecycleOwner) {
            mainActivity.showOrHideLoader(View.GONE)
        }

        viewModel.isDone.observe(viewLifecycleOwner) {
            Log.d("Thao Ho", viewModel.favoriteCities.size.toString())
            if(viewModel.favoriteCities.isNotEmpty()) {
                viewModel.errorVisibility.value = View.GONE
            } else {
                viewModel.errorVisibility.value = View.VISIBLE
            }
        }
        viewModel.errorVisibility.observe(viewLifecycleOwner) {
            if (it == View.GONE) {
                binding.recyclerviewCity.setHasFixedSize(true)
                binding.recyclerviewCity.layoutManager = LinearLayoutManager(view?.context)
                cityAdapter.setDataList(viewModel.favoriteCities.asSequence().map {
                    CityUIViewModel.from(it)
                }.toList())
                binding.recyclerviewCity.adapter = cityAdapter
                cityAdapter.setOnClickListener(object : CityAdapter.OnClickListener {
                    override fun onClick(city: CityUIViewModel) {
                        mainActivity.replaceFragment(DisplayWeatherFragment.newInstance(cityName = city.city, image = city.image))
                    }
                })
            }
            setErrorVisibility(it)
        }

        binding.txtSearch.textChanges().debounce(500).onEach { searchCity(it.toString()) }
            .launchIn(lifecycleScope)

        mainActivity.saveCurrentState("")
    }

    private fun setErrorVisibility(visibility: Int) {
        binding.empty1.visibility = visibility
        binding.empty2.visibility = visibility
        binding.empty3.visibility = visibility
        if (visibility == View.GONE) {
            binding.layoutCity.visibility = View.VISIBLE
            binding.recyclerviewSearch.visibility = View.GONE
        } else {
            binding.layoutCity.visibility = View.GONE
            binding.recyclerviewSearch.visibility = View.GONE
        }
        setErrorMessageDisplay()
    }

    private fun setErrorMessageDisplay() {
        if(viewModel.isSearch.value == true) {
            binding.empty2.text = viewModel.errorTitle_Search.value
            binding.empty3.text = viewModel.errorContent_Search.value
        } else {
            binding.empty2.text = viewModel.errorTitle_Favorite.value
            binding.empty3.text = viewModel.errorContent_Favorite.value
        }
    }

    private fun setSearchVisibility(visibility: Int) {
        binding.recyclerviewSearch.visibility = visibility
        binding.layoutCity.visibility = View.GONE

        if (visibility == View.GONE) {
            viewModel.isSearch.value = false
            binding.empty1.visibility = View.VISIBLE
            binding.empty2.visibility = View.VISIBLE
            binding.empty3.visibility = View.VISIBLE
        } else {
            viewModel.isSearch.value = true
            binding.empty1.visibility = View.GONE
            binding.empty2.visibility = View.GONE
            binding.empty3.visibility = View.GONE
        }
    }

    private fun searchCity(text: String) {
        var filterList = emptyList<CityUIViewModel>()

        if (!text.isNullOrBlank()) {
            filterList = AppUtils.getListCity()?.filter {
                it.city.startsWith(text, true)
            }?.take(8).orEmpty()
            filterList?.let {
                if (it.size != 0) {
                    setSearchVisibility(View.VISIBLE)
                } else {
                    setSearchVisibility(View.GONE)
                }
            } ?: run {
                setSearchVisibility(View.VISIBLE)
            }
            binding.recyclerviewSearch.setHasFixedSize(true)
            binding.recyclerviewSearch.layoutManager = LinearLayoutManager(context)
            searchAdapter.setDataList(filterList)
            binding.recyclerviewSearch.adapter = searchAdapter
            searchAdapter.setOnClickListener(object : SearchAdapter.OnClickListener {
                override fun onClick(city: CityUIViewModel) {
                    mainActivity.replaceFragment(DisplayWeatherFragment.newInstance(cityName = city.city, image = city.image))
                }
            })
        } else {
            setErrorVisibility(View.GONE)
        }
    }

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