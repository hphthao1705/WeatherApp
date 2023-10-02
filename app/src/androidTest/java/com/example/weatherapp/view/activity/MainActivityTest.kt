package com.example.weatherapp.view.activity

import android.util.Log
import androidx.compose.ui.test.hasText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.Test
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.weatherapp.R
import com.example.weatherapp.view.RecyclerViewItemCountAssertion
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.hasValue

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
        Thread.sleep(7000)
        onView(withId(R.id.txt_search)).perform(click())
        onView(withId(R.id.txt_search)).perform(typeText("a"))
        onView(withId(R.id.txt_search)).perform(typeText("n"))
        onView(withId(R.id.txt_search)).perform(typeText("a"))
        onView(withId(R.id.recyclerview_search)).check(matches(isDisplayed()))
        onView(withText("TIRANA")).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.txt_locationName)).check(matches(withText("Tirana")))
        onView(withId(R.id.btn_listcity)).perform((click()))
        onView(withId(R.id.recyclerview_favouritecity)).check(RecyclerViewItemCountAssertion(1));
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