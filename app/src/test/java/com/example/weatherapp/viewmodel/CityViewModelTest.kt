package com.example.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import com.example.weatherapp.data.model.Data
import com.example.weatherapp.data.model.PopulationCount
import io.mockk.MockKAnnotations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CityViewModelTest {
    @Mock
    lateinit var viewModel:CityViewModel
    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()
//    @get:Rule
//    val dispatcher = UnconfinedTestDispatcher()
    @BeforeEach
    fun setUp()
    {
//        MockKAnnotations.init(this, relaxed = true)
        viewModel = mock(CityViewModel())
//        Dispatchers.s(dispatcher)
    }
    @Test
    fun whenLiveDataHaveData_thenErrorIsEmpty() = runTest{
        //dispatcher.scheduler.advanceUntilIdle()
        val listPopulationCount:ArrayList<PopulationCount> = ArrayList()
        listPopulationCount.add(PopulationCount("2013","11370","Both Sexes","Final figure, complete"))
        val listData:ArrayList<Data> = ArrayList()
        listData.add(Data("MARIEHAMN","Åland Islands", listPopulationCount))
        //viewModel._liveData.postValue(listData)

        val citiesLiveData = MutableLiveData<List<Data>>()
//        citiesLiveData.value = listData.toList()
        citiesLiveData.postValue(listData)
        val errorLiveData:MutableLiveData<String>? = MutableLiveData()
        errorLiveData?.postValue("error")


        Mockito.`when`(viewModel._liveData).thenReturn(citiesLiveData)
        Mockito.`when`(viewModel._liveDataAPI).thenReturn(errorLiveData)

        val cities = viewModel._liveData
        val error = viewModel._liveDataAPI

        Assert.assertNotNull(cities)
        Assert.assertNull(error)

        //Mockito.verify(viewModel)._liveData
        //Mockito.verify(viewModel)._liveDataAPI
    }
    @AfterEach
    fun tearDown()
    {
        Dispatchers.resetMain()
    }
}