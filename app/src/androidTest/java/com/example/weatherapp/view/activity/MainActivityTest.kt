package com.example.weatherapp.view.activity

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.weatherapp.R
import com.example.weatherapp.utils.CountingIdlingResourceSingleton
import com.example.weatherapp.view.RecyclerViewItemCountAssertion
import com.example.weatherapp.view.adapter.CityAdapter
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Rule
    @JvmField
    //val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    val mainActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    //var activityRule = activityScenarioRule<MainActivity>()
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(CountingIdlingResourceSingleton.countingIdlingResource)
    }
    @Test
    fun testSearchOneCity_thenInRoomDatabaseWillHaveOneCity()
    {
        Thread.sleep(7000)
        onView(withId(R.id.txt_search)).perform(click())
        onView(withId(R.id.txt_search)).perform(typeText("ana"))
        onView(withId(R.id.recyclerview_search)).check(matches(isDisplayed()))
        onView(withText("TIRANA")).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.txt_locationName)).check(matches(withText("Tirana")))
        onView(withId(R.id.btn_listcity)).perform((click()))
        onView(withId(R.id.recyclerview_favouritecity)).check(RecyclerViewItemCountAssertion(1));
    }
    @Test
    fun testSearchThreeCity_thenInRoomDatabaseWillHaveThreeCity()
    {
        Thread.sleep(7000)
        //first city
        onView(withId(R.id.txt_search)).perform(click())
        onView(withId(R.id.txt_search)).perform(typeText("ana"))
        onView(withId(R.id.recyclerview_search)).check(matches(isDisplayed()))
        onView(withText("TIRANA")).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.txt_locationName)).check(matches(withText("Tirana")))
        //second city
        onView(withId(R.id.txt_search)).perform(click())
        onView(withId(R.id.txt_search)).perform(clearText(), typeText("pari"));
        onView(withId(R.id.recyclerview_search)).check(matches(isDisplayed()))
        onView(withText("PARIS")).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.txt_locationName)).check(matches(withText("Paris")))
        //third city
        onView(withId(R.id.txt_search)).perform(click())
        onView(withId(R.id.txt_search)).perform(clearText(), typeText("lond"));
        onView(withId(R.id.recyclerview_search)).check(matches(isDisplayed()))
        onView(withText("LONDON")).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.txt_locationName)).check(matches(withText("London")))

        onView(withId(R.id.btn_listcity)).perform((click()))
        onView(withId(R.id.recyclerview_favouritecity)).check(RecyclerViewItemCountAssertion(3));
    }
    @Test
    fun testClickOnCityDoNotHaveWeather_thenScreenDisplayNoData()
    {
        Thread.sleep(20000)
        onView(withId(R.id.recyclerview_city)).perform(scrollToPosition<CityAdapter.MyViewHolder>(100))
        Thread.sleep(2000)
        onView(withId(R.id.recyclerview_city)).perform(scrollToPosition<CityAdapter.MyViewHolder>(4))
        Thread.sleep(2000)
        onView(withText("Bejaïa")).perform(click())
        Thread.sleep(3000)
        onView(withText("NO RESULTS")).check(matches(isDisplayed()))
    }
    @Test
    fun testClickOnCityHaveWeather_thenScreenDisplayWeatherOfCity()
    {
        //Thread.sleep(9000)
        onView(withId(R.id.recyclerview_city)).perform(scrollToPosition<CityAdapter.MyViewHolder>(100))
        Thread.sleep(2000)
        onView(withId(R.id.recyclerview_city)).perform(scrollToPosition<CityAdapter.MyViewHolder>(2))
        Thread.sleep(2000)
        onView(withText("Annaba")).perform(click())
        Thread.sleep(3000)
        onView(withText("NO RESULTS")).check(matches(not(isDisplayed())))
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(CountingIdlingResourceSingleton.countingIdlingResource)
    }
}