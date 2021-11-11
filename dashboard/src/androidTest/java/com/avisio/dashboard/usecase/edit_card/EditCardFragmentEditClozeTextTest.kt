package com.avisio.dashboard.usecase.edit_card

import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.AnswerFlexBox
import com.google.android.material.chip.Chip
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsInstanceOf
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
                CardQuestionToken("TOKEN_1", CardQuestionTokenType.TEXT),
                CardQuestionToken("TOKEN_2", CardQuestionTokenType.QUESTION),
                CardQuestionToken("TOKEN_3", CardQuestionTokenType.TEXT)
            )),
            answer = CardAnswer(arrayListOf("ANSWER")),
            type = CardType.CLOZE_TEXT
        )
        val fragmentArgs = bundleOf(
            EditCardFragment.FRAGMENT_MODE_KEY to EditCardFragmentMode.EDIT_CARD.ordinal,
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
    fun changeCardTypeOnChipCloseClick() {
        scenario.onFragment {
            it.questionInput.replaceClozeTextByStandardQuestion()
        }
        onView(allOf(withClassName(`is`(EditText::class.java.name)), withText("TOKEN_1 TOKEN_2 TOKEN_3"))).check(matches(isDisplayed()))
    }

    @Test
    fun replaceClozeTextOnCardTypeChangedManually() {
        onView(withId(R.id.card_type_spinner)).check(matches(withSpinnerText(StringContains.containsString(CardType.CLOZE_TEXT.name))))
        onView(withId(R.id.card_type_spinner)).perform(ViewActions.click())
        Espresso.onData(allOf(`is`(IsInstanceOf.instanceOf(CardType.STANDARD::class.java)))).perform(ViewActions.click())
        onView(withId(R.id.card_type_spinner)).check(matches(withSpinnerText(StringContains.containsString(CardType.STANDARD.name))))
        onView(allOf(withClassName(`is`(EditText::class.java.name)), withText("TOKEN_1 TOKEN_2 TOKEN_3"))).check(matches(isDisplayed()))
    }

}