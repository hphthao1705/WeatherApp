package com.example.weatherapp.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.viewmodel.CityViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import androidx.test.rule.ActivityTestRule
import org.junit.Test
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.example.weatherapp.R

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private var viewmodelCity = mock(CityViewModel::class.java)
    @Rule
    @JvmField
    var activityRule = activityScenarioRule<MainActivity>()
    @Before
    fun setUp() {
    }
    @Test
    fun checkActivity()
    {
        onView(withId(R.id.txt_search)).perform(click())
    }
    @After
    fun tearDown() {
    }
}