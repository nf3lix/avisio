package com.avisio.dashboard.usecase.create_card

import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.common.workflow.CRUD
import com.avisio.dashboard.usecase.crud_card.common.EditCardFragment
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import com.avisio.dashboard.view_actions.BlockQuoteSpan.Companion.hasBlockQuote
import com.avisio.dashboard.view_actions.CodeSpan.Companion.hasCodeSpan
import com.avisio.dashboard.view_actions.HeadingSpan.Companion.hasHeadingSpan
import com.avisio.dashboard.view_actions.LinkSpan.Companion.hasLinkSpan
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

class EditCardMarkdownHandlerTest {

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

    @Test
    fun headingTest() {
        typeInQuestionEditText("# T")
        onView(withText("# T")).check(hasHeadingSpan(1))
        clearQuestionEditText()
        typeInQuestionEditText("## T")
        onView(withText("## T")).check(hasHeadingSpan(2))
        clearQuestionEditText()
        typeInQuestionEditText("### T")
        onView(withText("### T")).check(hasHeadingSpan(3))
    }

    @Test
    fun linkTest() {
        typeInQuestionEditText("[t](www.google.com)")
        onView(withText("[t](www.google.com)")).check(hasLinkSpan())
    }

    @Test
    fun codeTest() {
        typeInQuestionEditText("`c`")
        onView(withText("`c`")).check(hasCodeSpan())
    }

    @Test
    fun blockQuoteTest() {
        typeInQuestionEditText("> c")
        onView(withText("> c")).check(hasBlockQuote())
    }

    private fun typeInQuestionEditText(question: String) {
        typeInEditText(QuestionFlexBox::class.java.name, question)
    }

    private fun typeInEditText(className: String, input: String) {
        onView(
            allOf(
                withParent(withParent(withParent(withParent(withParent(withClassName(`is`(className))))))),
                withClassName(`is`(EditText::class.java.name))))
            .perform(typeText(input))
    }

    private fun clearQuestionEditText() {
        onView(
            allOf(
                withParent(withParent(withParent(withParent(withParent(withClassName(`is`(QuestionFlexBox::class.java.name))))))),
                withClassName(`is`(EditText::class.java.name))))
            .perform(clearText())
    }

}