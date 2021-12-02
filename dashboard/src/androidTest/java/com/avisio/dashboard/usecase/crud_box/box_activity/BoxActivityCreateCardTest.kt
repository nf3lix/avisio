package com.avisio.dashboard.usecase.crud_box.box_activity

import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.transfer.IntentKeys
import com.avisio.dashboard.common.persistence.CardDao
import com.avisio.dashboard.usecase.crud_box.read.BoxActivity
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.AnswerFlexBox
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.QuestionFlexBox
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoxActivityCreateCardTest {

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
        Intents.init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        cardDao = database.cardDao()
        cardDao.deleteAll()
    }

    @After
    fun releaseIntents() {
        Intents.release()
    }

    @Test
    fun createCardWorkflowTest() {
        onView(withId(R.id.fab_new_card)).perform(click())
        onView(withId(R.id.checkbox_create_new_card)).perform(click())
        onView(allOf(withParent(withParent(withParent(withParent(withParent(withClassName(`is`(
            AnswerFlexBox::class.java.name))))))), withClassName(`is`(
            EditText::class.java.name))))
            .perform(typeText("T"))
        onView(allOf(withParent(withParent(withParent(withParent(withParent(withClassName(`is`(
            QuestionFlexBox::class.java.name))))))),
            withClassName(`is`(EditText::class.java.name))))
            .perform(typeText("QUESTION"))
        onView(withId(R.id.fab_edit_card)).perform(click())
        onView(withResourceName("card_text_view")).check(matches(isDisplayed()))
    }

}