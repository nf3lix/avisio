package com.avisio.dashboard.usecase

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.create_box.CreateBoxActivity
import com.avisio.dashboard.usecase.crud_box.create_folder.CreateFolderActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_box)).perform(click())
        intended(hasComponent(CreateBoxActivity::class.java.name))
    }

    @Test
    fun startCreateFolderActivityOnFabClicked() {
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_folder)).perform(click())
        intended(hasComponent(CreateFolderActivity::class.java.name))
    }

}