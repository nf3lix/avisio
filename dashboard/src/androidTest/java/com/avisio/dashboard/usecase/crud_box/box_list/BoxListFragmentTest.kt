package com.avisio.dashboard.usecase.crud_box.box_list

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.persistence.TestUtils
import org.junit.After
import org.junit.Before
import org.junit.Test
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