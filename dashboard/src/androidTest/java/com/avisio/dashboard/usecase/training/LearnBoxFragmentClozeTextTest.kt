package com.avisio.dashboard.usecase.training

import android.content.Context
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.persistence.card.CardDao
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment
import com.avisio.dashboard.view_actions.WaitForView
import com.google.android.material.chip.Chip
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit


class LearnBoxFragmentClozeTextTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var cardDao: CardDao
    private lateinit var database: AppDatabase

    private lateinit var scenario: FragmentScenario<LearnBoxFragment>

    @Before
    fun initScenario() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        cardDao = database.cardDao()
        cardDao.deleteAll()
        val card1 = Card(
            boxId = 1,
            question = CardQuestion(arrayListOf(
                QuestionToken("TOKEN_1", QuestionTokenType.TEXT),
                QuestionToken("TOKEN_2", QuestionTokenType.QUESTION),
                QuestionToken("TOKEN_3", QuestionTokenType.TEXT))),
            type = CardType.CLOZE_TEXT
        )
        cardDao.insert(card1)
        Intents.init()
        val fragmentArgs = bundleOf(
            IntentKeys.BOX_OBJECT to ParcelableAvisioBox.createFromEntity(AvisioBox(id = 1)))
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_Avisio)
    }

    @After
    fun releaseIntents() {
        cardDao.deleteAll()
        Intents.release()
    }

    @Test
    fun showClozeTextChipOfQuestion() {
        onView(isRoot()).perform(WaitForView.withText("TOKEN_1", TimeUnit.SECONDS.toMillis(15)))
        onView(allOf(withClassName(`is`(TextView::class.java.name)), withText("TOKEN_1"))).check(matches(isDisplayed()))
        onView(allOf(withClassName(`is`(Chip::class.java.name)), withText(getQuestionPlaceholder("TOKEN_2")))).check(matches(isDisplayed()))
        onView(allOf(withClassName(`is`(TextView::class.java.name)), withText("TOKEN_3"))).check(matches(isDisplayed()))
    }

    @Test
    fun setDialogInputToChip() {
        setChipText("ANSWER_TOKEN")
        onView(allOf(withText("ANSWER_TOKEN"), withClassName(`is`(Chip::class.java.name)))).check(matches(isDisplayed()))
    }

    private fun setChipText(text: String) {
        onView(isRoot()).perform(WaitForView.withClassName(Chip::class.java.name, TimeUnit.SECONDS.toMillis(15)))
        onView(withClassName(`is`(Chip::class.java.name))).perform(click())
        onView(withClassName(`is`(AutoCompleteTextView::class.java.name))).perform(typeText(text))
        onView(withText(R.string.confirm_dialog_confirm_default)).perform(click())
    }

    private fun getQuestionPlaceholder(question: String): String {
        val stringBuilder = StringBuilder()
        for(i in 0..question.length) {
            stringBuilder.append(" ")
        }
        return stringBuilder.toString()
    }

}