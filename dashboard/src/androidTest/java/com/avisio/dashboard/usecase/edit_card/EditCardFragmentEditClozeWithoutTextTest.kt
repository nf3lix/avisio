package com.avisio.dashboard.usecase.edit_card

import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
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
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import com.avisio.dashboard.view_actions.Wait.Companion.waitFor
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

class EditCardFragmentEditClozeWithoutTextTest {

    private lateinit var scenario: FragmentScenario<EditCardFragment>

    @Before
    fun initScenario() {
        Intents.init()
        val card = Card(
            question = CardQuestion(arrayListOf(
                QuestionToken("TOKEN_1", QuestionTokenType.QUESTION)
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
    fun showTextIsRequiredWarningOnFabClicked() {
        onView(isRoot()).perform(waitFor(800))
        onView(withClassName(`is`(FloatingActionButton::class.java.name))).perform(click())
        onView(allOf(withText(R.string.edit_card_cloze_text_is_required), withParent(withParent(
            withParent(withParent(withParent(withClassName(`is`(QuestionFlexBox::class.java.name))))))))).check(matches(isDisplayed()))
    }

}