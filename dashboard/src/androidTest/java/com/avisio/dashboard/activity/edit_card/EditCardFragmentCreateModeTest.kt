package com.avisio.dashboard.activity.edit_card

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode
import com.avisio.dashboard.persistence.ToastMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test

class EditCardFragmentCreateModeTest {

    private lateinit var scenario: FragmentScenario<EditCardFragment>

    @Before
    fun initScenario() {
        Intents.init()
        val fragmentArgs = bundleOf(
            EditCardFragment.FRAGMENT_MODE_KEY to EditCardFragmentMode.CREATE_CARD.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to ParcelableCard.createFromEntity(Card(id = 1)))
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_AppCompat)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun inputFieldsEmptyByDefault() {
        onView(withId(R.id.card_question_input)).check(matches(withText("")))
        onView(withId(R.id.card_answer_input)).check(matches(withText("")))
    }

    @Test
    fun showErrorMessageIfFieldIsEmpty() {
        onView(withId(R.id.fab_edit_card)).perform(click())
        onView(withText(R.string.create_card_empty_question_answer)).inRoot(ToastMatcher().apply { matches(isDisplayed()) })
        onView(withId(R.id.fab_edit_card)).check(matches(isDisplayed()))

        onView(withId(R.id.card_question_input)).perform(typeText("QUESTION"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.fab_edit_card)).perform(click())
        onView(withText(R.string.create_card_empty_question_answer)).inRoot(ToastMatcher().apply { matches(isDisplayed()) })
        onView(withId(R.id.fab_edit_card)).check(matches(isDisplayed()))
    }

}