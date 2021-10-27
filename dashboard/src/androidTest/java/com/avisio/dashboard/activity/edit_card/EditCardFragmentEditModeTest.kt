package com.avisio.dashboard.activity.edit_card

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.CardQuestionToken
import com.avisio.dashboard.common.data.model.card.question.CardQuestionTokenType
import com.avisio.dashboard.common.ui.edit_card.EditCardFragment
import com.avisio.dashboard.common.ui.edit_card.EditCardFragmentMode
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
            answer = CardAnswer(arrayListOf("ANSWER"))
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
    fun inputFieldsEmptyByDefault() {
        onView(withId(R.id.card_question_input))
            .check(matches(withText("QUESTION_TOKEN")))
        onView(withId(R.id.card_answer_input))
            .check(matches(withText("ANSWER")))
    }

}