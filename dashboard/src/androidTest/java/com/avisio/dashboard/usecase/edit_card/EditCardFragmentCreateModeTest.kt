package com.avisio.dashboard.usecase.edit_card

import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragmentMode
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.hamcrest.core.IsNot.not
import org.hamcrest.core.StringContains.containsString
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
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_MaterialComponents)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun inputFieldsEmptyByDefault() {
        var questionInput: QuestionFlexBox
        var answerInput: AnswerFlexBox
        scenario.onFragment {
            questionInput = it.questionInput
            answerInput = it.answerInput
            assertThat(questionInput.getCardQuestion().getStringRepresentation(), `is`(""))
            assertThat(answerInput.getAnswer().getStringRepresentation(), `is`(""))
        }
    }

    @Test
    fun showErrorMessageIfFieldIsEmpty() {
        onView(withId(R.id.fab_edit_card)).perform(click())
        onView(withText(R.string.create_card_empty_answer)).check(matches(isDisplayed()))
        onView(withText(R.string.create_card_empty_question)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_edit_card)).check(matches(isDisplayed()))
        typeInQuestionEditText("QUESTION")
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.fab_edit_card)).perform(click())
        onView(withText(R.string.create_card_empty_answer)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_edit_card)).check(matches(isDisplayed()))
    }

    @Test
    fun showWarningIfCardTypeIsClozeTextAndAnswerInputNotEmpty() {
        typeInAnswerEditText("ANSWER")
        onView(withId(R.id.card_type_spinner)).perform(click())
        onView(withText(CardType.CLOZE_TEXT.name)).perform(click())
        onView(withId(R.id.card_type_spinner)).check(matches(withSpinnerText(containsString(CardType.CLOZE_TEXT.name))))
        onView(withText(R.string.edit_card_cloze_text_answer_is_ignored)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun removeWarningOfIgnoredAnswerOnCardTypeChanged() {
        onView(withId(R.id.card_type_spinner)).perform(click())
        onView(withText(CardType.STANDARD.name)).perform(click())
        onView(withId(R.id.card_type_spinner)).check(matches(withSpinnerText(containsString(CardType.STANDARD.name))))
        onView(withText(R.string.edit_card_cloze_text_answer_is_ignored)).check(matches(not(isDisplayed())))
    }

    @Test(expected = NoMatchingViewException::class)
    fun removeQuestionErrorTextOnKeyTyped() {
        onView(withId(R.id.fab_edit_card)).perform(click())
        onView(withText(R.string.create_card_empty_question)).check(matches(isDisplayed()))
        typeInQuestionEditText("QUESTION")
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withText(R.string.create_card_empty_question)).check(matches(not(isDisplayed())))
    }

    @Test(expected = NoMatchingViewException::class)
    fun removeAnswerErrorTextOnKeyTyped() {
        onView(withId(R.id.fab_edit_card)).perform(click())
        onView(withText(R.string.create_card_empty_answer)).check(matches(isDisplayed()))
        typeInAnswerEditText("ANSWER")
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withText(R.string.create_card_empty_answer)).check(matches(not(isDisplayed())))
    }

    private fun typeInQuestionEditText(question: String) {
        typeInEditText(QuestionFlexBox::class.java.name, question)
    }

    private fun typeInAnswerEditText(answer: String) {
        typeInEditText(AnswerFlexBox::class.java.name, answer)
    }

    private fun typeInEditText(className: String, input: String) {
        onView(
            allOf(
                withParent(withParent(withParent(withParent(withParent(withClassName(`is`(className))))))),
                withClassName(`is`(EditText::class.java.name))))
            .perform(typeText(input))
    }

}