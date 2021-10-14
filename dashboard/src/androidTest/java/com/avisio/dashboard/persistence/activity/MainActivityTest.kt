package com.avisio.dashboard.persistence.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.box_list.MainActivity
import com.avisio.dashboard.activity.create_box.CreateBoxActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup() {
        init()
    }

    @After
    fun releaseIntents() {
        release()
    }

    @Test
    fun startCreateBoxActivityOnFabClicked() {
        onView(withId(R.id.fab_new_box)).perform(click())
        intended(hasComponent(CreateBoxActivity::class.java.name))
    }

}