package com.example.weatherapp.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.`is`


class RecyclerViewItemCountAssertion(private var expectedCount: Int = 0): ViewAssertion {
    fun RecyclerViewItemCountAssertion(expectedCount: Int) {
        this.expectedCount = expectedCount
    }
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter!!.itemCount, `is`(expectedCount))
    }
}