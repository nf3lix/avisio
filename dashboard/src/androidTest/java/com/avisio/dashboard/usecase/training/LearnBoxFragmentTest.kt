package com.avisio.dashboard.usecase.training

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.sm.SMCardItem
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardType
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.persistence.card.CardDao
import com.avisio.dashboard.common.persistence.sm_card_items.SMCardItemDao
import com.avisio.dashboard.usecase.crud_box.read.BoxActivity
import com.avisio.dashboard.usecase.training.activity.question.QuestionLearnFlexBox
import com.avisio.dashboard.view_matchers.IsGoneMatcher
import com.avisio.dashboard.view_matchers.IsGoneMatcher.Companion.isGone
import com.avisio.dashboard.view_actions.WaitForView
import com.google.android.flexbox.FlexboxLayout
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class LearnBoxFragmentTest {

    private var intent: Intent
    private lateinit var cardDao: CardDao
    private lateinit var smCardItemDao: SMCardItemDao
    private lateinit var database: AppDatabase

    init {
        val box = ParcelableAvisioBox(1, "BOX_NAME", R.drawable.box_icon_language)
        intent = Intent(ApplicationProvider.getApplicationContext(), BoxActivity::class.java)
        intent.putExtra(IntentKeys.BOX_OBJECT, box)
    }

    @get:Rule
    val activityScenario = ActivityScenarioRule<BoxActivity>(intent)

    @Before
    fun setup() {
        Intents.init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        database.clearAllTables()
        cardDao = database.cardDao()
        smCardItemDao = database.smCardItemDao()
        val card1 = Card(boxId = 1, id = 1, type = CardType.STRICT, question = CardQuestion(arrayListOf(
            QuestionToken("QUESTION_1", QuestionTokenType.TEXT)
        )))
        cardDao.insert(card1)
        smCardItemDao.insert(SMCardItem(cardId = 1))
    }

    @After
    fun releaseIntents() {
        cardDao.deleteAll()
        smCardItemDao.deleteAll()
        Intents.release()
    }

    @Test
    fun showResultButtonsAfterQuestionResolved() {
        onView(withId(R.id.fab_learn)).perform(click())
        onView(isRoot()).perform(WaitForView.withText("QUESTION_1", TimeUnit.SECONDS.toMillis(15)))
        onView(withId(R.id.resolve_question_button)).perform(click())
        onView(withId(R.id.chipGroup)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun hideViewsAfterTrainingFinished() {
        onView(withId(R.id.fab_learn)).perform(click())
        onView(isRoot()).perform(WaitForView.withText("QUESTION_1", TimeUnit.SECONDS.toMillis(15)))
        onView(withId(R.id.resolve_question_button)).perform(click())
        onView(withText(R.string.learn_activity_result_easy)).perform(click())
        onView(withId(R.id.question_input_layout)).check(matches(IsGoneMatcher.isGone()))
    }

    @Test
    fun answerButtonIsDisabledOnCardLoad() {
        onView(withId(R.id.fab_learn)).perform(click())
        onView(withId(R.id.resolve_question_button)).check(matches(not(isEnabled())))
    }

    @Test
    fun answerButtonIsDisabledOnCardLoadEnd() {
        onView(withId(R.id.fab_learn)).perform(click())
        onView(isRoot()).perform(WaitForView.withText("QUESTION_1", TimeUnit.SECONDS.toMillis(15)))
        onView(withId(R.id.resolve_question_button)).check(matches(isEnabled()))
    }

    @Test
    fun hideProgressBarOnCardLoadEnd() {
        onView(withId(R.id.fab_learn)).perform(click())
        onView(isRoot()).perform(WaitForView.withText("QUESTION_1", TimeUnit.SECONDS.toMillis(15)))
        onView(withId(R.id.load_card_progressBar)).check(matches(isGone()))
    }

}