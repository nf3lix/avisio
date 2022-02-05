package com.avisio.dashboard.usecase.box_list

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.AvisioFolder
import com.avisio.dashboard.common.persistence.box.AvisioBoxDao
import com.avisio.dashboard.common.persistence.folder.FolderDao
import com.avisio.dashboard.usecase.MainActivity
import org.hamcrest.Matcher
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
    fun depth1Test() {
        depthTest(1)
    }

    @Test
    fun depth2Test() {
        depthTest(2)
    }

    @Test
    fun depth3Test() {
        depthTest(3)
    }

    @Test
    fun depth5Test() {
        depthTest(5)
    }

    @Test
    fun depth10Test() {
        depthTest(10)
    }

    @Test
    fun test() {
        boxDao.insertBox(AvisioBox(name = "BOX_1"))
        folderDao.insertFolder(AvisioFolder(id = 1, name = "FOLDER_1"))
        for(i in 1..1) {
            boxDao.insertBox(AvisioBox(name = "BOX_${i+1}", parentFolder = i.toLong()))
            folderDao.insertFolder(AvisioFolder(id = (i+1).toLong(), name = "FOLDER_${i+1}", parentFolder = i.toLong()))
        }
        onView(isRoot()).perform(waitFor(1000))
        onView(withText("FOLDER_1")).check(matches(isDisplayed()))
        onView(withText("BOX_1")).check(matches(isDisplayed()))
        onView(withText("FOLDER_1")).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        Espresso.pressBack()
    }

    private fun depthTest(depth: Int) {
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

    fun waitFor(millis: Long): ViewAction {
        return object : ViewAction {

            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "Wait for $millis milliseconds"
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(millis)
            }
        }
    }

}