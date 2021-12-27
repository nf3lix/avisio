package com.avisio.dashboard.usecase.training

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions
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
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.persistence.CardDao
import com.avisio.dashboard.persistence.ToastMatcher
import com.avisio.dashboard.usecase.training.activity.LearnBoxFragment
import com.google.android.flexbox.FlexboxLayout
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LearnBoxFragmentTest {

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
        val card1 = Card(boxId = 1, type = CardType.STRICT)
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
    fun showResultButtonsAfterQuestionResolved() {
        onView(withId(R.id.resolve_question_button)).perform(click())
        onView(withId(R.id.chipGroup)).check(matches(isDisplayed()))
    }

    @Test
    fun showCorrectAnswerAfterIncorrectInputResolved() {
        onView(withId(R.id.answer_edit_text)).perform(typeText("ANSWER"))
        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.resolve_question_button)).perform(click())
        onView(withId(R.id.correct_answer_input_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun hideViewsAfterTrainingFinished() {
        scenario.onFragment { fragment ->
            fragment.onTrainingFinished()
        }
        onView(withId(R.id.question_input_layout)).check(matches(not(isDisplayed())))
    }

    @Test(expected = NoMatchingViewException::class)
    fun resetQuestionTextLayoutOnResultOptionSelected() {
        scenario.onFragment { fragment ->
            fragment.onResultOptionSelected(QuestionResult.CORRECT)
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

}