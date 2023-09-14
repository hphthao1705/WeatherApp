package com.example.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.data.model.City
import com.example.weatherapp.repository.CityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import com.example.weatherapp.data.model.Data
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CityViewModelTest {
    @Mock
    lateinit var repository: CityRepository
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }
    @Test
    fun whenHaveNetwork_thenLoadDataFromAPISuccessfully() = runTest{
        val city: City = City(emptyList(),false, "s")
        Mockito.`when`(repository.loadCity()).thenReturn(flowOf(city.data))
        val sut = CityViewModel(repository)
        sut.loadCities()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut._liveData.hasObservers()
        Assert.assertEquals(false,result)
    }
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }
}