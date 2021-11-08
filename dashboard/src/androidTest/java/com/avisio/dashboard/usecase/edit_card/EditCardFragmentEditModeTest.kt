package com.avisio.dashboard.usecase.edit_card

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
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
import com.avisio.dashboard.persistence.ToastMatcher
import org.junit.After
import org.junit.Before
import org.junit.Test

class EditCardFragmentEditModeTest {

    private lateinit var scenario: FragmentScenario<EditCardFragment>

    @Before
    fun initScenario() {
        Intents.init()
        val card = Card(
            question = CardQuestion(arrayListOf(CardQuestionToken("QUESTION_TOKEN", CardQuestionTokenType.TEXT))),
            answer = CardAnswer(arrayListOf("ANSWER")),
            type = CardType.CLOZE_TEXT
        )
        val fragmentArgs = bundleOf(
            EditCardFragment.FRAGMENT_MODE_KEY to EditCardFragmentMode.EDIT_CARD.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to ParcelableCard.createFromEntity(card))
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_AppCompat)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun cardInformationSetByDefault() {
        onView(withId(R.id.card_question_input))
            .check(matches(withText("QUESTION_TOKEN")))
        onView(withId(R.id.card_answer_input))
            .check(matches(withText("ANSWER")))
        onView(withText(CardType.CLOZE_TEXT.name)).check(matches(isDisplayed()))
    }

    @Test
    fun showErrorMessageIfFieldIsEmpty() {
        onView(withId(R.id.card_question_input)).perform(clearText())
        onView(withId(R.id.card_answer_input)).perform(clearText())
        onView(withId(R.id.fab_edit_card)).perform(click())
        onView(withText(R.string.create_card_empty_answer)).check(matches(isDisplayed()))
        onView(withText(R.string.create_card_empty_question)).check(matches(isDisplayed()))
        onView(withId(R.id.fab_edit_card)).check(matches(isDisplayed()))
    }

}