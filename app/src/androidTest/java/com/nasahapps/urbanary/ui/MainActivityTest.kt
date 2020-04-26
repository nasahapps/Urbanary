package com.nasahapps.urbanary.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nasahapps.urbanary.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testInitialActivityState() {
        onView(withId(R.id.initialLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchingForDefinitions() {
        onView(withId(R.id.searchEditText)).perform(typeText("wat"))
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.recyclerView)).check { view, noViewFoundException ->
            (view as RecyclerView).adapter!!.itemCount > 0
        }
    }

}