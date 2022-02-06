package com.avisio.dashboard.usecase.box_list

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
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
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.persistence.box.AvisioBoxDao
import com.avisio.dashboard.common.persistence.folder.FolderDao
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.view_actions.Wait.Companion.waitFor
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DashboardFragmentFolderTest {

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
        database.clearAllTables()
    }

    @After
    fun releaseIntents() {
        database.clearAllTables()
        boxDao.deleteAll()
        folderDao.deleteAll()
        Intents.release()
    }

    @Test
    fun folderDepthTest() {
        testWithDepth(1)
        testWithDepth(2)
        testWithDepth(3)
        testWithDepth(5)
        testWithDepth(10)
    }

    @Test(expected = NoMatchingViewException::class)
    fun deleteFolderOptionNotVisibleInRootFolder() {
        onView(withContentDescription("More options")).perform(click())
        onView(withId(R.id.action_delete_folder)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun deleteFolderOptionNotVisibleOnReturnToRootFolder() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "FOLDER_1"))
        onView(withText("FOLDER_1")).perform(click())
        Espresso.pressBack()
        onView(withContentDescription("More options")).perform(click())
        onView(withId(R.id.action_delete_folder)).check(matches(isDisplayed()))
    }

    @Test
    fun deleteFolderOptionVisibleInNestedFolders() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "FOLDER_1"))
        onView(withText("FOLDER_1")).perform(click())
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.action_delete_folder)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun deleteFolderOnDeleteOptionSelected() {
        boxDao.insertBox(AvisioBox(name = "BOX_1"))
        folderDao.insertFolder(AvisioFolder(id = 1, name = "FOLDER_1"))
        onView(withText("FOLDER_1")).perform(click())
        onView(withContentDescription("More options")).perform(click())
        onView(withText(R.string.action_delete_folder)).perform(click())
        onView(withText("BOX_1")).check(matches(isDisplayed()))
        onView(withText("FOLDER_1")).check(matches(not(isDisplayed())))
    }

    private fun testWithDepth(depth: Int) {
        database.clearAllTables()
        boxDao.deleteAll()
        folderDao.deleteAll()
        boxDao.insertBox(AvisioBox(name = "BOX_1"))
        folderDao.insertFolder(AvisioFolder(id = 1, name = "FOLDER_1"))

        for(i in 1..depth) {
            boxDao.insertBox(AvisioBox(name = "BOX_${i+1}", parentFolder = i.toLong()))
            folderDao.insertFolder(AvisioFolder(id = (i+1).toLong(), name = "FOLDER_${i+1}", parentFolder = i.toLong()))
        }

        onView(isRoot()).perform(waitFor(1000))
        onView(withText("FOLDER_1")).check(matches(isDisplayed()))
        onView(withText("BOX_1")).check(matches(isDisplayed()))
        onView(withText("FOLDER_1")).perform(click())

        for(i in 1..depth) {
            onView(withText("FOLDER_${i+1}")).check(matches(isDisplayed()))
            onView(withText("BOX_${i+1}")).check(matches(isDisplayed()))
            onView(withText("FOLDER_${i+1}")).perform(click())
        }

        for(i in depth + 1 downTo 1) {
            Espresso.pressBack()
            onView(withText("FOLDER_$i")).check(matches(isDisplayed()))
            onView(withText("BOX_$i")).check(matches(isDisplayed()))
        }

        database.clearAllTables()
        boxDao.deleteAll()
        folderDao.deleteAll()
    }

}