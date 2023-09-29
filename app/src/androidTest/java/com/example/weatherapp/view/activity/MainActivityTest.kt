package com.example.weatherapp.view.activity

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
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
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import com.example.weatherapp.R

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    var activityRule = activityScenarioRule<MainActivity>()
    //var activityRule = createAndroidComposeRule<MainActivity>()
    //val rule = createComposeRule()
    @Before
    fun setUp() {
    }
    @Test
    fun testSearch_thenReturnToSearchFragment()
    {
        Thread.sleep(6000)
        onView(withId(R.id.txt_search)).perform(click())
        onView(withId(R.id.txt_search)).perform(typeText("a"))
        onView(withId(R.id.txt_search)).perform(typeText("n"))
        onView(withId(R.id.txt_search)).perform(typeText("a"))

        //onView(withId(R.id.framelayout)).check(matches(withId(R.id.fragment_search)))
    }
    @Test
    fun checkClickACity_thenDisplayWeather()
    {
        //onView(withId(R.id.txt_search)).perform(click())
        onView(withId(R.id.btn_listcity)).perform(click())
        //onView(check(matches(withId(R.id.fragment_favouritecity)))
    }
    @After
    fun tearDown() {
    }
}