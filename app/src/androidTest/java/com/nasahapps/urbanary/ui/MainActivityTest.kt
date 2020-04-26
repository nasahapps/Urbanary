package com.nasahapps.urbanary.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.nasahapps.urbanary.R
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Test
    fun testInitialActivityState() {
        onView(withId(R.id.initialLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchingForDefinitions() {
        onView(withId(R.id.searchEditText)).perform(typeText("wat"))
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
        onView(withId(R.id.recyclerView)).check { view, noViewFoundException ->
            (view as RecyclerView).adapter!!.itemCount > 0
        }
    }

    @Test
    fun testChangingSort() {
        onView(withId(R.id.searchEditText)).perform(typeText("wat"))
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        onView(withContentDescription("change sort")).perform(click())
        onView(withText("Most Thumbs Up"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText("Ok"))
            .inRoot(isDialog())
            .perform(click())

        scenarioRule.scenario.onActivity { activity ->
            Assert.assertEquals(
                "New sort value does not match", MainViewModel.Sort.THUMBS_UP,
                activity.viewModel.sort
            )
        }
    }

    @Test
    fun testStartingToChangeSortButCancellingAction() {
        var originalSort: MainViewModel.Sort? = null
        scenarioRule.scenario.onActivity { activity ->
            originalSort = activity.viewModel.sort
        }

        onView(withId(R.id.searchEditText)).perform(typeText("wat"))
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())

        onView(withContentDescription("change sort")).perform(click())
        onView(withText("Most Thumbs Up"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
        onView(withText("Cancel"))
            .inRoot(isDialog())
            .perform(click())

        scenarioRule.scenario.onActivity {
            Assert.assertEquals(
                "Sort value changed but shouldn't have", originalSort,
                it.viewModel.sort
            )
        }
    }

}