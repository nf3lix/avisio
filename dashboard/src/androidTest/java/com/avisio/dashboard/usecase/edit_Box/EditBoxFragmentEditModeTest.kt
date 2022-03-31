package com.avisio.dashboard.usecase.edit_Box

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_box.common.BoxIcon
import com.avisio.dashboard.usecase.crud_box.create_box.EditBoxFragment
import org.junit.After
import org.junit.Before
import org.junit.Test

class EditBoxFragmentEditModeTest {

    companion object {
        private const val BOX_NAME = "BOX_NAME_1"
    }
    private lateinit var scenario: FragmentScenario<EditBoxFragment>

    @Before
    fun initScenario() {
        Intents.init()
        val fragmentArgs = bundleOf(
            IntentKeys.BOX_OBJECT to ParcelableAvisioBox(1, BOX_NAME, BoxIcon.LANGUAGE.iconId),
            EditBoxFragment.BOX_CRUD_WORKFLOW to CRUD.UPDATE.ordinal)
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_AppCompat)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun setBoxNameInputTest() {
        onView(withId(R.id.box_name_edit_text)).check(matches(withText(BOX_NAME)))
    }

    @Test
    fun removeBoxNameTest() {
        onView(withId(R.id.box_name_edit_text)).perform(clearText())
        onView(withId(R.id.fab_edit_box)).perform(click())
        onView(withText(R.string.create_box_no_name_specified)).check(matches(isDisplayed()))
    }

    @Test
    fun showCardMenu() {
        onView(withText(R.string.box_activity_menu_edit)).check(matches(isDisplayed()))
    }

}