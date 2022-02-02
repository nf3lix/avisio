package com.avisio.dashboard.usecase.crud_box.box_activity.remaining_time

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.SMCardItem
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.persistence.CardDao
import com.avisio.dashboard.common.persistence.SMCardItemDao
import com.avisio.dashboard.usecase.crud_box.read.BoxActivity
import com.avisio.dashboard.view_actions.WaitForView
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class BoxActivityDueDateTest {

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
        cardDao = database.cardDao()
        smCardItemDao = database.smCardItemDao()
    }

    @After
    fun releaseIntents() {
        Intents.release()
        database.clearAllTables()
    }

    @Test
    fun cardDueInMin() {
        cardDao.insert(Card(boxId = 1, id = 1, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 1, dueDate = Date(System.currentTimeMillis() + 1000 * 60)))
        cardDao.insert(Card(boxId = 1, id = 2, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 2, dueDate = Date(System.currentTimeMillis() + 1000 * 60 * 2)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 1 min", TimeUnit.SECONDS.toMillis(15)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 2 min", TimeUnit.SECONDS.toMillis(15)))
    }

    @Test
    fun cardDueInH() {
        cardDao.insert(Card(boxId = 1, id = 3, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 3, dueDate = Date(System.currentTimeMillis() + 1000 * 60 * 60)))
        cardDao.insert(Card(boxId = 1, id = 4, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 4, dueDate = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 1 h", TimeUnit.SECONDS.toMillis(15)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 2 h", TimeUnit.SECONDS.toMillis(15)))
    }

    @Test
    fun cardDueInD() {
        cardDao.insert(Card(boxId = 1, id = 5, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 5, dueDate = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)))
        cardDao.insert(Card(boxId = 1, id = 6, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 6, dueDate = Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 2)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 1 Tag", TimeUnit.SECONDS.toMillis(15)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 2 Tagen", TimeUnit.SECONDS.toMillis(15)))
    }

    @Test
    fun cardDueInMonth() {
        cardDao.insert(Card(boxId = 1, id = 7, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 7, dueDate = Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)))
        cardDao.insert(Card(boxId = 1, id = 8, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 8, dueDate = Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 2)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 1 Monat", TimeUnit.SECONDS.toMillis(15)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 2 Monaten", TimeUnit.SECONDS.toMillis(15)))

    }

    @Test
    fun cardDueInYear() {
        cardDao.insert(Card(boxId = 1, id = 9, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 9, dueDate = Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)))
        cardDao.insert(Card(boxId = 1, id = 10, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 10, dueDate = Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 2)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 1 Jahr", TimeUnit.SECONDS.toMillis(15)))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Fällig in 2 Jahren", TimeUnit.SECONDS.toMillis(15)))
    }

    @Test
    fun notLearnedYet() {
        cardDao.insert(Card(boxId = 1, id = 11, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        smCardItemDao.insert(SMCardItem(cardId = 11))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Noch nicht gelernt", TimeUnit.SECONDS.toMillis(15)))
    }

    @Test
    fun notLearnedYetAndMissingSMCardItem() {
        cardDao.insert(Card(boxId = 1, id = 12, question = CardQuestion(arrayListOf(QuestionToken("TEST", QuestionTokenType.TEXT)))))
        onView(ViewMatchers.isRoot()).perform(WaitForView.withText("Noch nicht gelernt", TimeUnit.SECONDS.toMillis(15)))
    }

}