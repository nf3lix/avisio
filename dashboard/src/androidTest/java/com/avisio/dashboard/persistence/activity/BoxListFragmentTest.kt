package com.avisio.dashboard.persistence.activity

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
import com.avisio.dashboard.activity.box_list.AvisioBoxViewHolder
import com.avisio.dashboard.activity.box_list.BoxListFragment
import com.avisio.dashboard.common.data.model.AvisioBox
import com.avisio.dashboard.persistence.TestUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
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

    @Test
    fun addBoxToListTest() {
        scenario.onFragment { fragment ->
            fragment.newBoxReceived(AvisioBox(name = "TEST_1", createDate = Date(1600000000)))
        }
        onView(withId(R.id.box_list_recycler_view)).check(matches(TestUtils.atPosition(0, hasDescendant(
            withText("TEST_1")))))
    }

    @Test
    fun startBoxActivityOnItemClick() {
        scenario.onFragment { fragment ->
            fragment.newBoxReceived(AvisioBox(name = "TEST_1", createDate = Date(1600000000)))
        }
        onView(withId(R.id.box_list_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<AvisioBoxViewHolder>(0, click()))
        intended(hasComponent(BoxActivity::class.java.name))
    }


}