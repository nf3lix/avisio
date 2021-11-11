package com.avisio.dashboard.usecase.edit_card

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
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
import com.avisio.dashboard.common.ui.edit_card.input_flex_box.QuestionFlexBox
import org.hamcrest.Matchers
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
            type = CardType.CUSTOM
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
        var questionInput: QuestionFlexBox
        var answerInput: AnswerFlexBox
        scenario.onFragment {
            questionInput = it.questionInput
            answerInput = it.answerInput
            assertThat(questionInput.getCardQuestion().getStringRepresentation(), Matchers.`is`("QUESTION_TOKEN"))
            assertThat(answerInput.getAnswer().getStringRepresentation(), Matchers.`is`("ANSWER"))
        }
        onView(withText(CardType.CUSTOM.name)).check(matches(isDisplayed()))
    }

}