package com.avisio.dashboard.usecase.create_folder

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.usecase.crud_box.create_folder.CreateFolderActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateFolderTest {

    private var intent: Intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
    private lateinit var database: AppDatabase

    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    @Before
    fun setup() {
        Intents.init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        database.clearAllTables()
    }

    @After
    fun releaseIntents() {
        database.clearAllTables()
        Intents.release()
    }

    @Test
    fun correctAppBarTitleInCreateMode() {
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_folder)).perform(click())
        Intents.intended(IntentMatchers.hasComponent(CreateFolderActivity::class.java.name))
        onView(withText(R.string.fab_create_folder_label)).check(matches(isDisplayed()))
    }

    @Test
    fun createFolderInRootTest() {
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_folder)).perform(click())
        onView(withId(R.id.folder_name_edit_text)).perform(typeText("FOLDER_1"))
        onView(withId(R.id.fab_edit_folder)).perform(click())
        onView(withText("FOLDER_1")).check(matches(isDisplayed()))
        onView(withText(R.string.main_activity_app_bar_title)).check(matches(isDisplayed()))
        onView(withText("FOLDER_1")).check(matches(isDisplayed()))
    }

    @Test
    fun createNestedFolderTest() {
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_folder)).perform(click())
        onView(withId(R.id.folder_name_edit_text)).perform(typeText("FOLDER_1"))
        onView(withId(R.id.fab_edit_folder)).perform(click())
        onView(withText("FOLDER_1")).perform(click())
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_folder)).perform(click())
        onView(withId(R.id.folder_name_edit_text)).perform(typeText("FOLDER_2"))
        onView(withId(R.id.fab_edit_folder)).perform(click())
        onView(withText("FOLDER_2")).check(matches(isDisplayed()))
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withId(R.id.fab_new_folder)).perform(click())
        onView(withId(R.id.folder_name_edit_text)).perform(typeText("FOLDER_3"))
        onView(withId(R.id.fab_edit_folder)).perform(click())
        onView(withText("FOLDER_2")).check(matches(isDisplayed()))
        onView(withText("FOLDER_3")).check(matches(isDisplayed()))
        Espresso.pressBack()
        onView(withText("FOLDER_1")).perform(click())
    }

}