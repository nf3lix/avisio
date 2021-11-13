package com.avisio.dashboard.usecase.edit_card

import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test

class CreateStandardCardInstantFeedbackTest {

    private lateinit var scenario: FragmentScenario<EditCardFragment>

    @Before
    fun initScenario() {
        Intents.init()
        val fragmentArgs = bundleOf(
            EditCardFragment.CARD_CRUD_WORKFLOW to CRUD.CREATE.ordinal,
            EditCardFragment.CARD_OBJECT_KEY to ParcelableCard.createFromEntity(Card(id = 1)))
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_MaterialComponents)
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test(expected = NoMatchingViewException::class)
    fun hideSuccessIconByDefault() {
        onView(withTagValue(`is`(R.drawable.ic_check))).check(matches(not(isDisplayed())))
    }

    @Test
    fun showSuccessIconOnValidQuestionInput() {
        typeInEditText(QuestionFlexBox::class.java.name, "TEST")
        onView(withTagValue(`is`(R.drawable.ic_check))).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun removeSuccessIconOnClearQuestionInput() {
        clearInput(QuestionFlexBox::class.java.name)
        onView(withTagValue(`is`(R.drawable.ic_check))).check(matches(not(isDisplayed())))
    }

    @Test
    fun showSuccessIconOnValidAnswerInput() {
        typeInEditText(AnswerFlexBox::class.java.name, "TEST")
        onView(withTagValue(`is`(R.drawable.ic_check))).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun removeSuccessIconOnClearAnswerInput() {
        clearInput(AnswerFlexBox::class.java.name)
        onView(withTagValue(`is`(R.drawable.ic_check))).check(matches(not(isDisplayed())))
    }

    private fun typeInEditText(className: String, input: String) {
        onView(allOf(withParent(withParent(withParent(withParent(withParent(withClassName(`is`(className))))))),
            withClassName(`is`(EditText::class.java.name))))
            .perform(typeText(input))
    }

    private fun clearInput(className: String) {
        onView(allOf(withParent(withParent(withParent(withParent(withParent(withClassName(`is`(className))))))),
            withClassName(`is`(EditText::class.java.name))))
            .perform(clearText())
    }

}