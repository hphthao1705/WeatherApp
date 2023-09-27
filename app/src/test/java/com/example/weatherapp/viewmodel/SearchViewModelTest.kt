package com.example.weatherapp.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build.VERSION_CODES.Q
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.local.SearchDatabase
import com.example.weatherapp.data.local.dao.SearchDAO
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.IOException
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Q])
//@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest
{
    private lateinit var searchDatabase: SearchDatabase
    private lateinit var searchDAO:SearchDAO

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @BeforeAll
    fun setUp()
    {
        val context = RuntimeEnvironment.getApplication()
        searchDatabase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),SearchDatabase::class.java).allowMainThreadQueries().build()
        //searchDatabase = Room.inMemoryDatabaseBuilder(context,SearchDatabase::class.java).fallbackToDestructiveMigration().build()
        searchDAO = searchDatabase.getSearchDao()
        //MockitoAnnotations.initMocks(this)
    }
    @Test
    fun whenInsertNewCity_thenDatabaseWillHaveThatCity() = runBlocking {
        val search = Search("Ho Phuong Thao")
        searchDAO.insertCity(search)

        val result = searchDAO.getAllCity().getOrAwaitValue()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Ho Phuong Thao", result[0].name)
    }

    @Test
    fun whenDeleteCity_thenDatabaseIsEmpty() = runBlocking {
        val search = "Ho Phuong Thao"
        searchDAO.deleteCity(search)

        val result = searchDAO.getAllCity().getOrAwaitValue()

        Assert.assertEquals(0, result.size)
    }
    @AfterEach
    @Throws(IOException::class)
    fun closeDb() {
        searchDatabase.close()
    }
}