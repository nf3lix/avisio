package com.avisio.dashboard.usecase.edit_card

import android.widget.EditText
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
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
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
            type = CardType.STANDARD
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
        onView(withText(R.string.card_type_standard)).check(matches(isDisplayed()))
    }

    @Test
    fun editCardMenuIsDisplayed() {
        onView(withContentDescription("More options")).check(matches(isDisplayed()))
    }

    @Test
    fun showConfirmDialogOnDeleteClicked() {
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.edit_card_delete)).perform(click())
        onView(withText(R.string.edit_card_delete_card_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun finishActivityOnConfirmDeletionClicked() {
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.edit_card_delete)).perform(click())
        onView(withText(R.string.confirm_dialog_confirm_default)).perform(click())
    }

    @Test
    fun closeConfirmDeletionDialogOnCancelClicked() {
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.edit_card_delete)).perform(click())
        onView(withText(R.string.confirm_dialog_cancel_default)).perform(click())
        onView(withText(R.string.edit_card_action_bar_title)).check(matches(isDisplayed()))
    }

    @Test
    fun finishActivityOnFabClicked() {
        typeInAnswerEditText("TEST")
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.fab_edit_card)).perform(click())
    }

    private fun typeInAnswerEditText(answer: String) {
        typeInEditText(AnswerFlexBox::class.java.name, answer)
    }

    private fun typeInEditText(className: String, input: String) {
        onView(allOf(
            withParent(withParent(withParent(withParent(withParent(withClassName(`is`(className))))))),
            withClassName(`is`(EditText::class.java.name))))
        .perform(typeText(input))
    }

}