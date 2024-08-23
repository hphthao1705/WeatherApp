package com.example.weatherapp.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.example.weatherapp.R
import com.example.weatherapp.view.RecyclerViewItemCountAssertion
import com.example.weatherapp.view.city.adapter.CityAdapter
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    val DEFAULT_TIMEOUT = 20000
    @Rule
    @JvmField
    //val activityScenario = ActivityScenario.launch(MainActivity::class.java)
    //var activityRule = activityScenarioRule<MainActivity>()
    val mainActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)
    val device:UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    @Before
    fun setUp() {
        //IdlingRegistry.getInstance().register(CountingIdlingResourceSingleton.countingIdlingResource)
    }
    @Test
    fun test3_SearchOneCity_thenInRoomDatabaseWillHaveOneCity()
    {
        Thread.sleep(7000)
        onView(withId(R.id.recyclerview_favouritecity)).check(RecyclerViewItemCountAssertion(1));
        onView(withText("Annaba")).check(matches(isDisplayed()))
    }
    @Test
    fun test4_SearchMoreTwoCity_thenInRoomDatabaseWillHaveThreeCity()
    {
        Thread.sleep(7000)
        //first city
//        onView(withId(R.id.txt_search)).perform(click())
//        onView(withId(R.id.txt_search)).perform(clearText(), typeText("pari"));
        onView(withId(R.id.recyclerview_search)).check(matches(isDisplayed()))
        onView(withText("PARIS")).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.txt_locationName)).check(matches(withText("Paris")))
        //second city
//        onView(withId(R.id.txt_search)).perform(click())
//        onView(withId(R.id.txt_search)).perform(clearText(), typeText("lond"));
        onView(withId(R.id.recyclerview_search)).check(matches(isDisplayed()))
        onView(withText("LONDON")).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.txt_locationName)).check(matches(withText("London")))

        onView(withId(R.id.btn_listcity)).perform((click()))
        onView(withId(R.id.recyclerview_favouritecity)).check(RecyclerViewItemCountAssertion(3));
        onView(withText("Annaba")).check(matches(isDisplayed()))
        onView(withText("PARIS")).check(matches(isDisplayed()))
        onView(withText("LONDON")).check(matches(isDisplayed()))
    }
    @Test
    fun test2_SearchACityDoNotHaveWeather_thenScreenDisplayNoData()
    {
        Thread.sleep(17000)
//        onView(withId(R.id.txt_search)).perform(click())
//        onView(withId(R.id.txt_search)).perform(typeText("bej"))
        onView(withId(R.id.recyclerview_search)).check(matches(isDisplayed()))
        onView(withText("Bejaïa")).perform(click())
        Thread.sleep(3000)
        onView(withText("NO RESULTS")).check(matches(isDisplayed()))
    }
    @Test
    fun test1_SearchACityHaveWeather_thenScreenDisplayWeatherOfCity()
    {
        onView(withId(R.id.progressbar_start)).check(matches(isDisplayed()))
        Thread.sleep(20000)
        onView(withId(R.id.recyclerview_city)).check(matches((isDisplayed())))
        onView(withId(R.id.recyclerview_city)).perform(scrollToPosition<CityAdapter.MyViewHolder>(100))
        Thread.sleep(2000)
        onView(withId(R.id.recyclerview_city)).perform(scrollToPosition<CityAdapter.MyViewHolder>(2))
        Thread.sleep(2000)
        onView(withText("Annaba")).perform(click())
        Thread.sleep(3000)
        onView(withText("NO RESULTS")).check(matches(not(isDisplayed())))
    }
    @Test
    fun test5_ClickOnACityInFavouriteCity_thenScreenDisplayWeatherOfThatCity()
    {
        Thread.sleep(7000)
        onView(withText("Annaba")).perform(click())
        Thread.sleep(3000)
        onView(withText("NO RESULTS")).check(matches(not(isDisplayed())))
    }
    @After
    fun tearDown() {
        //IdlingRegistry.getInstance().unregister(CountingIdlingResourceSingleton.countingIdlingResource)
    }
}