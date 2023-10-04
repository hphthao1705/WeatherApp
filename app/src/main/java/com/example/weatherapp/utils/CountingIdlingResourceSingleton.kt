package com.example.weatherapp.utils

import androidx.test.espresso.idling.CountingIdlingResource

object CountingIdlingResourceSingleton {
    private const val RESOURCE = "LOAD"

    @JvmField val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}