package com.avisio.dashboard.usecase.edit_card

import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
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
import com.google.android.material.chip.Chip
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.AllOf
import org.hamcrest.core.Is
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class QuestionFlexBoxTest {

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

    @Test
    fun showClozeTextChipByDefault() {
        onView(allOf(withClassName(`is`(EditText::class.java.name)), withText("ghuk"))).check(matches(isDisplayed()))
    }

}