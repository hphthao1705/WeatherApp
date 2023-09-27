package com.example.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.data.model.PopulationCount
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CityViewModelTest {
    @Mock
    lateinit var viewModel:CityViewModel

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()
    @BeforeEach
    fun setUp()
    {
        MockitoAnnotations.initMocks(this)
        //viewModel = CityViewModel()
    }
    @Test
    fun whenLiveDataHaveData_thenErrorIsEmpty() = runTest{
        val listPopulationCount:ArrayList<PopulationCount> = ArrayList()
        listPopulationCount.add(PopulationCount("2013","11370","Both Sexes","Final figure, complete"))
        val listData:ArrayList<Data> = ArrayList()
        listData.add(Data("MARIEHAMN","Åland Islands", listPopulationCount))

        val citiesLiveData = MutableLiveData<List<Data>>()
        citiesLiveData.postValue(listData)
        val errorLiveData:MutableLiveData<String>? = MutableLiveData()
        errorLiveData?.postValue("error")


        Mockito.`when`(viewModel._liveData).thenReturn(citiesLiveData)
        Mockito.`when`(viewModel._liveDataAPI).thenReturn(errorLiveData)

        val cities = viewModel._liveData
        val error = viewModel._liveDataAPI

        Assert.assertNotNull(cities)
        Assert.assertNotNull(error)

        Mockito.verify(viewModel)._liveData
        Mockito.verify(viewModel)._liveDataAPI
    }
    @Test
    fun whenLiveDataIsEmpty_thenErrorHavaData() = runTest{
        val citiesLiveData = MutableLiveData<List<Data>>()
        citiesLiveData.postValue(null)
        val errorLiveData:MutableLiveData<String>? = MutableLiveData()
        errorLiveData?.postValue("error")

        Mockito.`when`(viewModel._liveData).thenReturn(citiesLiveData)
        Mockito.`when`(viewModel._liveDataAPI).thenReturn(errorLiveData)

        val cities = viewModel._liveData
        val error = viewModel._liveDataAPI

        Assert.assertEquals(citiesLiveData,cities)
        Assert.assertEquals(errorLiveData,error)

        Mockito.verify(viewModel)._liveData
        Mockito.verify(viewModel)._liveDataAPI
    }
}