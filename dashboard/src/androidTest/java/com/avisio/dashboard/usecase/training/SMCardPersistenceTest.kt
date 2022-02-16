package com.avisio.dashboard.usecase.training

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
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
import com.avisio.dashboard.common.persistence.card.CardDao
import com.avisio.dashboard.common.persistence.sm_card_items.SMCardItemDao
import com.avisio.dashboard.persistence.dao.DaoTest
import com.avisio.dashboard.usecase.crud_box.read.BoxActivity
import com.avisio.dashboard.view_actions.WaitForView
import org.junit.*
import java.util.*
import java.util.concurrent.TimeUnit

class SMCardPersistenceTest : DaoTest() {

    private var intent: Intent = Intent(ApplicationProvider.getApplicationContext(), BoxActivity::class.java)
    private lateinit var cardDao: CardDao
    private lateinit var smCardDao: SMCardItemDao
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
        cardDao = database.cardDao()
        smCardDao = database.smCardItemDao()
        database.clearAllTables()
        cardDao.insert(
            Card(id = 1, boxId = 1, createDate = Date(1600000000000), question = CardQuestion(
            arrayListOf(QuestionToken("TEST_QUESTION", QuestionTokenType.TEXT))), answer = CardAnswer(
            arrayListOf("TEST_ANSWER"))
        )
        )
    }

    @After
    fun release() {
        database.clearAllTables()
        Intents.release()
    }

    @Test
    fun smCardItemDaoEmptyBeforeFirstTraining() {
        val size = smCardDao.getSize()
        Assert.assertEquals(size, 0)
    }

    @Test
    fun forgettingCurvesAreCreatedBeforeFirstTraining() {
        Espresso.onView(ViewMatchers.withId(R.id.fab_learn)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.isRoot())
            .perform(WaitForView.withText("TEST_QUESTION", TimeUnit.SECONDS.toMillis(15)))
        val size = smCardDao.getSize()
        Assert.assertEquals(size, 1)
    }

}