package com.example.weatherapp.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.local.SearchDatabase
import com.example.weatherapp.data.local.dao.SearchDAO
import com.example.weatherapp.data.local.entities.Search
import com.example.weatherapp.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
@RunWith(AndroidJUnit4::class)
//@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest
{
    private lateinit var searchDatabase: SearchDatabase
    private lateinit var searchDAO:SearchDAO

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        searchDatabase = Room.inMemoryDatabaseBuilder(context,SearchDatabase::class.java).allowMainThreadQueries().build()
        searchDAO = searchDatabase.getSearchDao()
    }
    @Test
    @Throws(Exception::class)
    fun whenInsertNewCity_thenDatabaseWillHaveThatCity() = runBlocking {
        val search = Search("Ho Phuong Thao")
        searchDAO.insertCity(search)

        val result = searchDAO.getAllCity().getOrAwaitValue()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Ho Phuong Thao", result[0].name)
    }

    @Test
    @Throws(Exception::class)
    fun whenDeleteCity_thenDatabaseIsEmpty() = runBlocking {
        val search = "Ho Phuong Thao"
        searchDAO.deleteCity(search)

        val result = searchDAO.getAllCity().getOrAwaitValue()

        Assert.assertEquals(0, result.size)
    }
    @After
    @Throws(IOException::class)
    fun closeDb() {
        searchDatabase.close()
    }
}