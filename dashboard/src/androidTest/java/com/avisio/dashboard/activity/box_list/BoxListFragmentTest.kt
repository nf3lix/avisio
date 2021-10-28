package com.avisio.dashboard.activity.box_list

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.box_activity.BoxActivity
import com.avisio.dashboard.activity.box_activity.CardViewHolder
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.persistence.TestUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

class BoxListFragmentTest {

    private lateinit var scenario: FragmentScenario<BoxListFragment>

    @Before
    fun initScenario() {
        init()
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_AppCompat)
    }

    @After
    fun releaseIntents() {
        release()
    }

}