package com.avisio.dashboard.usecase.edit_Box

import androidx.appcompat.view.menu.ListMenuItemView
import androidx.appcompat.widget.MenuPopupWindow
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.usecase.crud_box.common.BoxIcon
import com.avisio.dashboard.usecase.crud_box.common.EditBoxFragment
import com.avisio.dashboard.usecase.crud_box.common.EditBoxFragmentMode
import com.avisio.dashboard.persistence.IndexMatcher
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class EditBoxFragmentCreateModeTest {

    companion object {
        private const val BOX_NAME = "BOX_NAME_1"
    }
    private lateinit var scenario: FragmentScenario<EditBoxFragment>

    @Before
    fun initScenario() {
        Intents.init()
        val fragmentArgs = bundleOf(
            IntentKeys.BOX_OBJECT to ParcelableAvisioBox(1, BOX_NAME, BoxIcon.DEFAULT.iconId),
            EditBoxFragment.FRAGMENT_MODE_KEY to EditBoxFragmentMode.CREATE_BOX.ordinal)
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_AppCompat)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun setBoxNameInputTest() {
        onView(withId(R.id.box_name_edit_text))
            .check(matches(withText("")))
    }

    @Test
    fun setBoxIconTest() {
        onView(withId(R.id.box_icon_imageview))
            .check(matches(withTagValue(equalTo(R.drawable.box_icon_default))))
    }

    @Test
    fun updateImageIconOnOptionSelected() {
        onView(withId(R.id.select_icon_button)).perform(click())
        onView(IndexMatcher(withClassName(containsString(ListMenuItemView::class.java.simpleName)), 1)).perform(click());
        onView(withId(R.id.box_icon_imageview)).check(matches(withTagValue(equalTo(R.drawable.box_icon_language))))
    }

    @Test(expected = NoMatchingViewException::class)
    fun closePopupMenuOnOptionSelected() {
        onView(withId(R.id.select_icon_button)).perform(click())
        onView(IndexMatcher(withClassName(containsString(ListMenuItemView::class.java.simpleName)), 1)).perform(click());
        onView(allOf(withClassName(containsString(MenuPopupWindow.MenuDropDownListView::class.java.simpleName)))).check(
            matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun showErrorIfBoxNameIsEmpty() {
        onView(withId(R.id.select_icon_button)).perform(click())
        onView(withText(R.string.create_box_no_name_specified)).check(matches(isDisplayed()))
        onView(withId(R.id.box_name_edit_text)).perform(typeText("NAME"))
        onView(withText(R.string.create_box_no_name_specified)).check(matches(not(isDisplayed())))
    }

}