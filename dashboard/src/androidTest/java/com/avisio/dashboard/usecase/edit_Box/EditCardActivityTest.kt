package com.avisio.dashboard.usecase.edit_Box

import android.content.Intent
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_card.update.EditCardActivity
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.AnswerFlexBox
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditCardActivityTest {

    private var intent: Intent =
        Intent(ApplicationProvider.getApplicationContext(), EditCardActivity::class.java)

    init {
        intent.putExtra(EditCardFragment.CARD_OBJECT_KEY, ParcelableCard.createFromEntity(Card(boxId = 1)))
    }

    @get:Rule
    val activityScenario = ActivityScenarioRule<EditCardActivity>(intent)

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun showWarningOnBackPressed() {
        onView(allOf(withParent(withParent(withParent(withParent(withParent(withClassName(`is`(AnswerFlexBox::class.java.name))))))), withClassName(`is`(
            EditText::class.java.name))))
            .perform(typeText("TEST"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        Espresso.pressBack()
        onView(withText(R.string.create_card_cancel_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun disposeWarningOnCancel() {
        onView(allOf(withParent(withParent(withParent(withParent(withParent(withClassName(`is`(AnswerFlexBox::class.java.name))))))), withClassName(`is`(
            EditText::class.java.name))))
            .perform(typeText("TEST"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        Espresso.pressBack()
        onView(withText(R.string.confirm_dialog_cancel_default)).perform(click())
        onView(withId(R.id.fab_edit_card)).check(matches(isDisplayed()))
    }

}