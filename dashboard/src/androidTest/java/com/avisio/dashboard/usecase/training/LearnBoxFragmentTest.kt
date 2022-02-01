package com.avisio.dashboard.usecase.training

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.SMCardItem
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.persistence.CardDao
import com.avisio.dashboard.common.persistence.SMCardItemDao
import com.avisio.dashboard.view_actions.ToastMatcher
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment
import com.avisio.dashboard.view_actions.IsGoneMatcher.Companion.isGone
import com.avisio.dashboard.view_actions.WaitForView
import com.google.android.flexbox.FlexboxLayout
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class LearnBoxFragmentTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var cardDao: CardDao
    private lateinit var smCardDao: SMCardItemDao
    private lateinit var database: AppDatabase

    private lateinit var scenario: FragmentScenario<LearnBoxFragment>

    @Before
    fun initScenario() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        cardDao = database.cardDao()
        smCardDao = database.smCardItemDao()
        val card1 = Card(boxId = 1, id = 1, type = CardType.STRICT, question = CardQuestion(arrayListOf(
            QuestionToken("QUESTION_1", QuestionTokenType.TEXT)
        )))
        cardDao.insert(card1)
        smCardDao.insert(SMCardItem(cardId = 1))
        Intents.init()
        val fragmentArgs = bundleOf(
            IntentKeys.BOX_OBJECT to ParcelableAvisioBox.createFromEntity(AvisioBox(id = 1)))
        scenario = launchFragmentInContainer(fragmentArgs = fragmentArgs, themeResId = R.style.Theme_Avisio)
    }

    @After
    fun releaseIntents() {
        cardDao.deleteAll()
        smCardDao.deleteAll()
        Intents.release()
    }

    @Test
    fun showResultButtonsAfterQuestionResolved() {
        onView(isRoot()).perform(WaitForView.withText("QUESTION_1", TimeUnit.SECONDS.toMillis(15)))
        onView(withId(R.id.resolve_question_button)).perform(click())
        onView(withId(R.id.chipGroup)).check(matches(isDisplayed()))
    }

    @Test
    fun hideViewsAfterTrainingFinished() {
        scenario.onFragment { fragment ->
            fragment.onTrainingFinished()
        }
        onView(withId(R.id.question_input_layout)).check(matches(not(isDisplayed())))
    }

    @Test
    fun resetQuestionTextLayoutOnResultOptionSelected() {
        onView(isRoot()).perform(WaitForView.withText("QUESTION_1", TimeUnit.SECONDS.toMillis(15)))
        scenario.onFragment { fragment ->
            fragment.onResultOptionSelected(QuestionResult.EASY)
        }
        onView(withParent(withClassName(`is`(
            FlexboxLayout::class.java.name)))).check(matches(withText("")))
    }

    @Test
    fun showToastOnCardLoadFailure() {
        scenario.onFragment { fragment ->
            fragment.onCardLoadFailure("TEST_MESSAGE")
        }
        onView(withText("TEST_MESSAGE")).inRoot(ToastMatcher().apply { matches(isDisplayed()) })
    }

    @Test
    fun answerButtonIsDisabledOnCardLoad() {
        onView(withId(R.id.resolve_question_button)).check(matches(not(isEnabled())))
    }

    @Test
    fun answerButtonIsDisabledOnCardLoadEnd() {
        onView(isRoot()).perform(WaitForView.withText("QUESTION_1", TimeUnit.SECONDS.toMillis(15)))
        onView(withId(R.id.resolve_question_button)).check(matches(isEnabled()))
    }

    @Test
    fun showProgressBarOnCardLoad() {
        onView(withId(R.id.load_card_progressBar)).check(matches(isDisplayed()))
    }

    @Test
    fun hideProgressBarOnCardLoadEnd() {
        onView(isRoot()).perform(WaitForView.withText("QUESTION_1", TimeUnit.SECONDS.toMillis(15)))
        onView(withId(R.id.load_card_progressBar)).check(matches(isGone()))
    }

}