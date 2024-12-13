package com.example.weatherapp.view.city

import android.content.Context
import android.os.Bundle
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
import com.example.weatherapp.view.city.adapter.CityAdapter
import com.example.weatherapp.view.city.adapter.SearchAdapter
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
    private val viewModel: CityViewModel by lazy {
        ViewModelProvider(
            this,
            CityViewModel.ViewModelFactory(this.requireActivity().application)
        )[CityViewModel::class.java]
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
        binding.lifecycleOwner = this

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

        mainActivity.saveCurrentState("")

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