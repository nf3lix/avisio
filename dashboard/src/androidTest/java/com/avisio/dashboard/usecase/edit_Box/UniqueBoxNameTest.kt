package com.avisio.dashboard.usecase.edit_Box

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.persistence.box.AvisioBoxDao
import com.avisio.dashboard.common.persistence.folder.FolderDao
import com.avisio.dashboard.usecase.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UniqueBoxNameTest {

    private var intent: Intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
    private lateinit var boxDao: AvisioBoxDao
    private lateinit var folderDao: FolderDao
    private lateinit var database: AppDatabase

    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    @Before
    fun setup() {
        Intents.init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        boxDao = database.boxDao()
        folderDao = database.folderDao()
        boxDao.deleteAll()
        folderDao.deleteAll()
    }

    @After
    fun releaseIntents() {
        boxDao.deleteAll()
        folderDao.deleteAll()
        Intents.release()
    }

    @Test
    fun createBoxTest() {
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_box)).perform(click())
        onView(withId(R.id.box_name_edit_text)).perform(typeText("TEST"))
        onView(withId(R.id.fab_edit_box)).perform(click())
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_box)).perform(click())
        onView(withId(R.id.box_name_edit_text)).perform(typeText("TEST"))
        onView(withId(R.id.fab_edit_box)).perform(click())
        onView(withText(R.string.create_box_duplicate_name_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun editBoxTest() {
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_box)).perform(click())
        onView(withId(R.id.box_name_edit_text)).perform(typeText("TEST"))
        onView(withId(R.id.fab_edit_box)).perform(click())

        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_box)).perform(click())
        onView(withId(R.id.box_name_edit_text)).perform(typeText("TEST_2"))
        onView(withId(R.id.fab_edit_box)).perform(click())
        onView(withText("TEST_2")).perform(longClick())
        onView(withText(R.string.edit_selected_dashboard_items)).perform(click())
        onView(withId(R.id.box_name_edit_text)).perform(clearText())
        onView(withId(R.id.box_name_edit_text)).perform(typeText("TEST"))
        onView(withId(R.id.fab_edit_box)).perform(click())
        onView(withText(R.string.create_box_duplicate_name_dialog_message)).check(matches(isDisplayed()))
    }

}