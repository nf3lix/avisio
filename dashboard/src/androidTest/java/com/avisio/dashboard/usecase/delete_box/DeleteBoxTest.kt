package com.avisio.dashboard.usecase.delete_box

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
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.persistence.box.AvisioBoxDao
import com.avisio.dashboard.usecase.MainActivity
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteBoxTest {

    private var intent: Intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
    private lateinit var boxDao: AvisioBoxDao
    private lateinit var database: AppDatabase

    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    @Before
    fun setup() {
        Intents.init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        boxDao = database.boxDao()
        boxDao.deleteAll()
        boxDao.insertBox(AvisioBox(name = "TEST_BOX", id = 1))
    }

    @After
    fun releaseIntents() {
        boxDao.deleteAll()
        Intents.release()
    }

    @Test(expected = NoMatchingViewException::class)
    fun deleteBoxTest() {
        onView(withText("TEST_BOX")).perform(click())
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.box_activity_menu_delete)).perform(click())
        onView(withText(R.string.confirm_dialog_confirm_default)).perform(click())
        onView(withText(R.string.main_activity_app_bar_title)).check(matches(isDisplayed()))
        onView(withText("TEST_BOX")).check(matches(isDisplayed()))
    }

}