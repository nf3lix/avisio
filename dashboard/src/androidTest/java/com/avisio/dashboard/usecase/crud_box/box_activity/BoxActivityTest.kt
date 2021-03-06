package com.avisio.dashboard.usecase.crud_box.box_activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.update.update_box.EditBoxActivity
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.question.CardQuestion
import com.avisio.dashboard.common.data.model.card.question.QuestionToken
import com.avisio.dashboard.common.data.model.card.question.QuestionTokenType
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.persistence.card.CardDao
import com.avisio.dashboard.usecase.crud_box.read.BoxActivity
import com.avisio.dashboard.view_actions.WaitForView
import org.junit.*
import java.util.concurrent.TimeUnit

class BoxActivityTest {

    private var intent: Intent
    private lateinit var cardDao: CardDao
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
        init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        database.clearAllTables()
        cardDao = database.cardDao()
        cardDao.deleteAll()
    }

    @After
    fun releaseIntents() {
        database.clearAllTables()
        release()
    }

    @Test
    fun startCreateCardActivityOnFabClicked() {
        onView(withId(R.id.fab_new_card)).perform(click())
        onView(isRoot()).perform(WaitForView.withText("Neue Karte", TimeUnit.SECONDS.toMillis(15)))
    }

    @Test
    fun showDialogOnDeleteMenuItemClicked() {
        openContextualActionModeOverflowMenu()
        onView(withText(R.string.box_activity_menu_delete)).perform(click())
        onView(withText(R.string.delete_box_confirm_dialog_message)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun closeDeleteDialogOnCancelClicked() {
        openContextualActionModeOverflowMenu()
        onView(withText(R.string.box_activity_menu_delete)).perform(click())
        onView(withText(R.string.confirm_dialog_cancel_default)).perform(click())
        onView(withText(R.string.delete_box_confirm_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun startEditBoxActivityOnMenuItemSelected() {
        openContextualActionModeOverflowMenu()
        onView(withText(R.string.box_activity_menu_edit)).perform(click())
        intended(hasComponent(EditBoxActivity::class.java.name))
    }

    @Test
    fun boxMenuIsDisplayed() {
        onView(withContentDescription("More options")).check(matches(isDisplayed()))
    }

    @Test
    fun cardQueueTest() {
        for(i in 0 until 4) {
            cardDao.insert(Card(boxId = 1, question = CardQuestion(arrayListOf(QuestionToken(i.toString(), QuestionTokenType.TEXT)))))
        }
        onView(withId(R.id.fab_learn)).perform(click())

        for(i in 0 until 3) {
            processCard(i.toString())
        }
        Espresso.pressBack()
        onView(withId(R.id.fab_learn)).perform(click())
        processCard("3")
    }

    private fun processCard(cardQuestion: String) {
        onView(isRoot()).perform(WaitForView.withText(cardQuestion, TimeUnit.SECONDS.toMillis(15)))
        onView(withId(R.id.resolve_question_button)).perform(click())
        onView(withText(R.string.learn_activity_result_easy)).perform(click())
    }

}