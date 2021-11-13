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
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
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
            question = CardQuestion(arrayListOf(QuestionToken("QUESTION_TOKEN", QuestionTokenType.TEXT))),
            answer = CardAnswer(arrayListOf("ANSWER")),
            type = CardType.CUSTOM
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