package com.avisio.dashboard.usecase.box_list

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.view_matchers.IsGoneMatcher.Companion.isGone
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardFragmentFabTest {

    private var intent: Intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun createFabsHiddenByDefault() {
        onView(withId(R.id.fab_new_box)).check(matches(isGone()))
        onView(withId(R.id.fab_new_folder)).check(matches(isGone()))
    }

    @Test
    fun showCreateFabsOnMenuExpanded() {
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_box)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_new_folder)).check(matches(isDisplayed()))
    }

    @Test
    fun hideCreateFabsOnMenuClosed() {
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_box)).check(matches(isGone()))
        onView(withId(R.id.fab_new_folder)).check(matches(isGone()))
    }

}