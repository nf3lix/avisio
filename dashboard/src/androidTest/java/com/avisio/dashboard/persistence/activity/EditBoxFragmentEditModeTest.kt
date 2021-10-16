package com.avisio.dashboard.persistence.activity

import androidx.appcompat.widget.MenuPopupWindow
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.box_activity.BoxActivity
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox
import com.avisio.dashboard.common.ui.BoxIcon
import com.avisio.dashboard.common.ui.EditBoxFragment
import com.avisio.dashboard.common.ui.EditBoxFragmentMode
import com.avisio.dashboard.persistence.ToastMatcher
import org.hamcrest.Matchers.*
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
            EditBoxFragment.BOX_OBJECT_KEY to ParcelableAvisioBox(1, BOX_NAME, BoxIcon.LANGUAGE.iconId),
            EditBoxFragment.FRAGMENT_MODE_KEY to EditBoxFragmentMode.EDIT_BOX.ordinal)
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_AppCompat)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun setBoxNameInputTest() {
        onView(withId(R.id.box_name_input)).check(matches(withText(BOX_NAME)))
    }

    @Test
    fun setBoxIconTest() {
        onView(withId(R.id.box_icon_imageview)).check(matches(withTagValue(equalTo(R.drawable.box_icon_language))))
    }

    @Test
    fun clickFabTest() {
        onView(withId(R.id.fab_edit_box)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_edit_box)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(BoxActivity::class.java.name))
    }

    @Test
    fun removeBoxNameTest() {
        onView(withId(R.id.box_name_input)).perform(clearText())
        onView(withId(R.id.fab_edit_box)).perform(click())
        onView(withText(R.string.create_box_no_name_specified)).inRoot(ToastMatcher().apply {
            matches(isDisplayed())
        })
    }

    @Test
    fun openPopupOnItemViewClick() {
        onView(withId(R.id.box_icon_imageview)).perform(click())
        onView(allOf(withClassName(containsString(MenuPopupWindow.MenuDropDownListView::class.java.simpleName)))).check(matches(
            isDisplayed()))
    }

    @Test
    fun openPopupOnButtonClick() {
        onView(withId(R.id.select_icon_button)).perform(click())
        onView(allOf(withClassName(containsString(MenuPopupWindow.MenuDropDownListView::class.java.simpleName)))).check(matches(
            isDisplayed()))
    }

}