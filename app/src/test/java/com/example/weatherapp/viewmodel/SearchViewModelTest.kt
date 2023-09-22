package com.example.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.data.local.entities.Search
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
class SearchViewModelTest
{
    @Mock
    lateinit var viewModel: SearchViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @BeforeEach
    fun setUp()
    {
        MockitoAnnotations.initMocks(this)
    }
    @Test
    fun whenDatabaseHaveData_thenDisplayTenCitiesRecently() = runTest {
        val listRoom:ArrayList<Search> = ArrayList()
        listRoom.add(Search("Ho Phuong Thao"))
        listRoom.add(Search("Ho Phuong Thao"))
        listRoom.add(Search("Ho Phuong Thao"))
        listRoom.add(Search("Ho Phuong Thao"))
        listRoom.add(Search("Ho Phuong Thao"))

        val liveData:MutableLiveData<List<Search>> = MutableLiveData()
        liveData.postValue(listRoom)

        Mockito.`when`(viewModel.getAllNote()).thenReturn(liveData)

        val getFavouriteCity = viewModel.getAllNote()

        Assert.assertNotNull(getFavouriteCity)
        Assert.assertEquals(5, getFavouriteCity?.value?.size)
    }
}