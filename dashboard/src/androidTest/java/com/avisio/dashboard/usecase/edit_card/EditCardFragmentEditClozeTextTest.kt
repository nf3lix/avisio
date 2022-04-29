package com.avisio.dashboard.usecase.edit_card

import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.google.android.material.chip.Chip
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.hamcrest.core.StringContains
import org.junit.After
import org.junit.Before
import org.junit.Test

class EditCardFragmentEditClozeTextTest {

    private lateinit var scenario: FragmentScenario<EditCardFragment>

    @Before
    fun initScenario() {
        Intents.init()
        val card = Card(
            question = CardQuestion(arrayListOf(
                QuestionToken("TOKEN_1", QuestionTokenType.TEXT),
                QuestionToken("TOKEN_2", QuestionTokenType.QUESTION),
                QuestionToken("TOKEN_3", QuestionTokenType.TEXT)
            )),
            answer = CardAnswer(arrayListOf("ANSWER")),
            type = CardType.CLOZE_TEXT
        )
        val fragmentArgs = bundleOf(
            EditCardFragment.CARD_CRUD_WORKFLOW to CRUD.UPDATE.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to ParcelableCard.createFromEntity(card))
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_MaterialComponents)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun answerInputGoneByDefault() {
        onView(withClassName(`is`(AnswerFlexBox::class.java.name))).check(matches(not(isDisplayed())))
    }

    @Test
    fun showClozeTextChipByDefault() {
        onView(allOf(withClassName(`is`(EditText::class.java.name)), withText("TOKEN_1"))).check(matches(isDisplayed()))
        onView(allOf(withClassName(`is`(Chip::class.java.name)), withText("TOKEN_2"))).check(matches(isDisplayed()))
        onView(allOf(withClassName(`is`(EditText::class.java.name)), withText("TOKEN_3"))).check(matches(isDisplayed()))
    }

    @Test
    fun replaceClozeTextOnCardTypeChangedManually() {
        onView(withId(R.id.card_type_spinner)).check(matches(withSpinnerText(StringContains.containsString("Lückentext"))))
        onView(withId(R.id.card_type_spinner)).perform(click())
        onView(withText(R.string.card_type_strict)).perform(click())
        onView(withId(R.id.card_type_spinner)).check(matches(withSpinnerText(StringContains.containsString("Strenge Abfrage"))))
        onView(allOf(withClassName(`is`(EditText::class.java.name)), withText("TOKEN_1 TOKEN_2 TOKEN_3"))).check(matches(isDisplayed()))
    }

    @Test
    fun showWarningOnBackPressedAfterChange() {
        onView(withText("TOKEN_1")).perform(typeText("TEST"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        pressBack()
        onView(withText(R.string.create_card_apply_changes)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun disposeUnsavedChangesWarningOnCancel() {
        onView(withText("TOKEN_1")).perform(typeText("TEST"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        pressBack()
        onView(withText(R.string.confirm_dialog_cancel_default)).perform(click())
        onView(withText(R.string.create_card_apply_changes)).check(matches(not(isDisplayed())))
    }

    @Test(expected = NoActivityResumedException::class)
    fun finishActivityOnBackPressedWithoutChanges() {
        pressBack()
    }

}