package com.avisio.dashboard.usecase.training

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.CardAnswer
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.data.transfer.setBoxObject
import com.avisio.dashboard.common.persistence.CardDao
import com.avisio.dashboard.common.persistence.ForgettingCurveDao
import com.avisio.dashboard.usecase.crud_box.read.BoxActivity
import com.avisio.dashboard.view_actions.WaitForView
import org.junit.*
import java.util.*
import java.util.concurrent.TimeUnit

class ForgettingCurvePersistenceTest {

    private var intent: Intent = Intent(ApplicationProvider.getApplicationContext(), BoxActivity::class.java)
    private lateinit var forgettingCurveDao: ForgettingCurveDao
    private lateinit var cardDao: CardDao
    private lateinit var database: AppDatabase

    init {
        intent.setBoxObject(AvisioBox(id = 1, name = "TEST_BOX"))
    }

    @get:Rule
    val activityScenario = ActivityScenarioRule<BoxActivity>(intent)

    @Before
    fun setup() {
        Intents.init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        forgettingCurveDao = database.forgettingCurveDao()
        cardDao = database.cardDao()
        database.clearAllTables()
        cardDao.insert(Card(id = 1, boxId = 1, createDate = Date(1600000000000), question = CardQuestion(
            arrayListOf(QuestionToken("TEST_QUESTION", QuestionTokenType.TEXT))), answer = CardAnswer(
            arrayListOf("TEST_ANSWER"))
        ))
    }

    @After
    fun release() {
        database.clearAllTables()
        Intents.release()
    }

    @Test
    fun forgettingCurveTableIsEmptyBeforeFirstTraining() {
        val size = forgettingCurveDao.getSize()
        Assert.assertEquals(size, 0)
    }

    @Test
    fun forgettingCurvesAreCreatedBeforeFirstTraining() {
        onView(withId(R.id.fab_learn)).perform(click())
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("TEST_QUESTION", TimeUnit.SECONDS.toMillis(15)))
        val size = forgettingCurveDao.getSize()
        Assert.assertEquals(size, 400)
    }

}