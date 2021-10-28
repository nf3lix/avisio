package com.avisio.dashboard.activity.box_activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.box_list.AvisioBoxViewHolder
import com.avisio.dashboard.activity.crud_card.create_card.CreateCardActivity
import com.avisio.dashboard.activity.crud_card.edit_card.EditCardActivity
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.persistence.CardDao
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class BoxActivityTest {

    private lateinit var intent: Intent
    private lateinit var cardDao: CardDao
    private lateinit var database: AppDatabase

    init {
        val box = ParcelableAvisioBox(1, "BOX_NAME", 1)
        intent = Intent(ApplicationProvider.getApplicationContext(), BoxActivity::class.java)
        intent.putExtra(BoxActivity.PARCELABLE_BOX_KEY, box)
    }


    @get:Rule
    val activityScenario = ActivityScenarioRule<BoxActivity>(intent)

    @Before
    fun setup() {
        init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        cardDao = database.cardDao()
        cardDao.deleteAll()
    }

    @After
    fun releaseIntents() {
        release()
    }

    @Test
    fun startCreateCardActivityOnFabClicked() {
        onView(withId(R.id.fab_new_card)).perform(click())
        intended(hasComponent(CreateCardActivity::class.java.name))
    }

    @Test
    fun startEditCardActivityOnItemClicked() {
        val card1 = Card(boxId = 1)
        cardDao.insert(card1)
        onView(withId(R.id.card_list_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<AvisioBoxViewHolder>(0, click()))
        intended(hasComponent(EditCardActivity::class.java.name))
    }

}