package com.example.weatherapp.view.fragment

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentCityBinding
import com.example.weatherapp.utils.AppUtils
import com.example.weatherapp.view.activity.MainActivity
import com.example.weatherapp.view.adapter.CityAdapter
import com.example.weatherapp.view.adapter.SearchAdapter
import com.example.weatherapp.viewmodel.CityViewModel
import com.example.weatherapp.viewmodel.uiViewModel.CityUIViewModel
import com.google.android.gms.common.internal.Preconditions.checkMainThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.androidx.viewmodel.ext.android.getViewModel

class CityFragment : Fragment() {

    private lateinit var binding: FragmentCityBinding
    private var cityAdapter: CityAdapter = CityAdapter()
    private var searchAdapter: SearchAdapter = SearchAdapter()
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

        viewModel.errorVisibility.observe(viewLifecycleOwner) {
            if (it == View.GONE) {
                binding.recyclerviewCity.setHasFixedSize(true)
                binding.recyclerviewCity.layoutManager = GridLayoutManager(view?.context, 2)
                cityAdapter.setDataList(AppUtils.getListCity())
                binding.recyclerviewCity.adapter = cityAdapter
                cityAdapter.setOnClickListener(object : CityAdapter.OnClickListener {
                    override fun onClick(city: CityUIViewModel) {
                        mainActivity.replaceFragment(DisplayWeatherFragment.newInstance(cityName = city.city))
                    }
                })
            }
            setErrorVisibility(it)
        }

        binding.txtSearch.textChanges().debounce(2000).onEach { searchCity(it.toString()) }
            .launchIn(lifecycleScope)
        mainActivity.saveCurrentState("")
    }

    fun setErrorVisibility(visibility: Int) {
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
    }

    fun setSearchVisibility(visibility: Int) {
        binding.recyclerviewSearch.visibility = visibility
        binding.layoutCity.visibility = View.GONE

        if (visibility == View.GONE) {
            binding.empty1.visibility = View.VISIBLE
            binding.empty2.visibility = View.VISIBLE
            binding.empty3.visibility = View.VISIBLE
        } else {
            binding.empty1.visibility = View.GONE
            binding.empty2.visibility = View.GONE
            binding.empty3.visibility = View.GONE
        }
    }

    private fun searchCity(text: String) {
        var filterList = emptyList<CityUIViewModel>()

        if (!text.isNullOrBlank()) {
            filterList = AppUtils.getListCity().filter {
                it.city.startsWith(text, true)
            }.take(8)
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
                    mainActivity.replaceFragment(DisplayWeatherFragment.newInstance(cityName = city.city))
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