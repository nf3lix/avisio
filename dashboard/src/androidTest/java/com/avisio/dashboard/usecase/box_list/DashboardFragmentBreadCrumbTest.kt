package com.avisio.dashboard.usecase.box_list

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.persistence.box.AvisioBoxDao
import com.avisio.dashboard.common.persistence.folder.FolderDao
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.view_actions.Wait.Companion.waitFor
import com.avisio.dashboard.view_matchers.IsGoneMatcher.Companion.isGone
import com.google.android.material.textview.MaterialTextView
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardFragmentBreadCrumbTest {

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
    fun showRootFolderIconByDefault() {
        onView(withTagValue(`is`(R.drawable.ic_home))).check(matches(isDisplayed()))
    }

    @Test
    fun displayCurrentPathInBreadCrumb() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2", parentFolder = 1))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "F_3", parentFolder = 2))
        folderDao.insertFolder(AvisioFolder(id = 4, name = "F_4", parentFolder = 3))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(click())
        onView(allOf(withText("F_1"), withClassName(`is`(TextView::class.java.name)))).check(matches(isDisplayed()))
        onView(withText("F_2")).perform(click())
        onView(allOf(withText("F_2"), withClassName(`is`(TextView::class.java.name)))).check(matches(isDisplayed()))
        onView(withText("F_3")).perform(click())
        onView(allOf(withText("F_3"), withClassName(`is`(TextView::class.java.name)))).check(matches(isDisplayed()))
        onView(withText("F_4")).perform(click())
        onView(allOf(withText("F_4"), withClassName(`is`(TextView::class.java.name)))).check(matches(isDisplayed()))
    }

    @Test
    fun switchFolderByBreadCrumbClicked() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2", parentFolder = 1))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "F_3", parentFolder = 2))
        folderDao.insertFolder(AvisioFolder(id = 4, name = "F_4", parentFolder = 3))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("F_2")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("F_3")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("F_4")).perform(click())
        onView(allOf(withText("F_3"), withClassName(`is`(TextView::class.java.name)))).perform(click())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_4")).check(matches(isDisplayed()))
        onView(allOf(withText("F_2"), withClassName(`is`(TextView::class.java.name)))).perform(click())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_3")).check(matches(isDisplayed()))
        onView(allOf(withText("F_1"), withClassName(`is`(TextView::class.java.name)))).perform(click())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_2")).check(matches(isDisplayed()))
        onView(withTagValue(`is`(R.drawable.ic_home))).perform(click())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).check(matches(isDisplayed()))
    }

    @Test
    fun setBreadCrumbCorrectlyAfterNestedItemCreated() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("F_2")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.fab_expand)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.fab_new_box)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.box_name_edit_text)).perform(typeText("T_1"))
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.fab_edit_box)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("T_1")).check(matches(isDisplayed()))
        onView(withTagValue(`is`(R.drawable.ic_home))).check(matches(isDisplayed()))
        onView(withText("F_1")).check(matches(isDisplayed()))
        onView(withText("F_2")).check(matches(isDisplayed()))
    }

    @Test
    fun disableBreadCrumbClicksIfItemsAreSelected() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2", parentFolder = 1))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "F_3", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(click())

        onView(withText("F_2")).perform(longClick())
        onView(withTagValue(`is`(R.drawable.ic_home))).perform(click())
        onView(withText("F_2")).check(matches(isDisplayed()))
        onView(withText("F_3")).check(matches(isDisplayed()))

        onView(withText("F_1")).perform(longClick())
        onView(withTagValue(`is`(R.drawable.ic_home))).perform(click())
        onView(withText("F_1")).check(matches(isDisplayed()))
    }

    @Test
    fun hideBreadCrumbWhenDashboardIsEmpty() {
        onView(withId(R.id.breadCrumb)).check(matches(isGone()))
        onView(withId(R.id.no_matching_item_label)).check(matches(isDisplayed()))
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.breadCrumb)).check(matches(isDisplayed()))
        onView(withId(R.id.no_matching_item_label)).check(matches(isGone()))
        folderDao.deleteAll()
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.breadCrumb)).check(matches(isGone()))
        onView(withId(R.id.no_matching_item_label)).check(matches(isDisplayed()))
    }
}