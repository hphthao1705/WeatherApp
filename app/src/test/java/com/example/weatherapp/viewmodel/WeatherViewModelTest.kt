package com.example.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.model.Condition
import com.example.weatherapp.data.model.Current
import com.example.weatherapp.data.model.Location
import com.example.weatherapp.data.model.Weather
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WeatherViewModelTest {
    @Mock
    lateinit var viewModel: WeatherViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @BeforeEach
    fun setUp() {
       MockitoAnnotations.initMocks(this)
    }
    @Test
    fun whenLiveDataHaveData_thenErrorIsEmpty() = runTest{
        val cityName = "HaNoi"
        val location = Location("Hanoi",21.03, "Vietnam","1695353560",105.85,"Asia/Bangkok","","2023-09-22 10:32")
        val condition = Condition(1003,"//cdn.weatherapi.com/weather/64x64/day/116.png","Partly cloudy")
        val current = Current(1695353400,condition, 38.3,100.9,7.9,4.9,67,1,
            "2023-09-22 10:30",1695353400, 0.0,
            0.02,0.0,1012.0,"32",89.6,7.0,10.0,6.0,154,"SSE",3.6,2.2)
        val weather = Weather(current,location)

        var liveData:MutableLiveData<Weather> = MutableLiveData()
        liveData.postValue(weather)

        Mockito.`when`(viewModel._liveData).thenReturn(liveData)
        Mockito.`when`(viewModel.error).thenReturn("")

        val getWeather = viewModel._liveData
        val getError = viewModel.error

        Assert.assertEquals("", getError)
        Assert.assertNotNull(getWeather)
        //so sanh country
        Assert.assertEquals(cityName, getWeather.value)

        Mockito.verify(viewModel)._liveData
        Mockito.verify(viewModel).error
    }
}