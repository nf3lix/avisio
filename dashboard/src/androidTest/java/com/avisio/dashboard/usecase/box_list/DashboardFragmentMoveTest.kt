package com.avisio.dashboard.usecase.box_list

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
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
import com.avisio.dashboard.view_matchers.IsGoneMatcher.Companion.isGone
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardFragmentMoveTest {

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
    fun showFinishWorkflowFabInMoveWorkflow() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(withId(R.id.fab_cancel_workflow)).check(matches(isDisplayed()))
    }

    @Test
    fun showFinishWorkflowButtonMenuItemInMoveWorkflow() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(withId(R.id.action_stop_workflow)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun hideFinishWorkflowButtonsOnExitFabClicked() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(withId(R.id.fab_cancel_workflow)).perform(click())
        onView(withId(R.id.fab_cancel_workflow)).check(matches(isGone()))
        onView(withId(R.id.action_stop_workflow)).check(matches(isGone()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun hideFinishWorkflowButtonsOnExitMenuItemClicked() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(withId(R.id.action_stop_workflow)).perform(click())
        onView(withId(R.id.fab_cancel_workflow)).check(matches(isGone()))
        onView(withId(R.id.action_stop_workflow)).check(matches(isGone()))
    }

    @Test
    fun showSelectionButtonsOnExitMoveWorkflow() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(withId(R.id.action_stop_workflow)).perform(click())
        onView(withId(R.id.btn_edit_item)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_move_all)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_delete_all)).check(matches(isDisplayed()))
    }

    @Test
    fun doNotShowEditItemButtonOnExitMoveWorkflowIfMultipleItemsAreSelected() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_2"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(withText("FOLDER_2")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(withId(R.id.action_stop_workflow)).perform(click())
        onView(withId(R.id.btn_edit_item)).check(matches(isGone()))
        onView(withId(R.id.btn_move_all)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_delete_all)).check(matches(isDisplayed()))
    }

    @Test
    fun hideMoveButtonsOnFinishFabClicked() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_2"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_3"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.fab_cancel_workflow)).perform(click())
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_1")))))).check(matches(isGone()))
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_2")))))).check(matches(isGone()))
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_3")))))).check(matches(isGone()))
    }

    @Test
    fun hideMoveButtonsOnFinishMenuItemClicked() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_2"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_3"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.action_stop_workflow)).perform(click())
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_1")))))).check(matches(isGone()))
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_2")))))).check(matches(isGone()))
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_3")))))).check(matches(isGone()))
    }

    @Test
    fun hideMoveButtonsOnLastSelectedItemUnselected() {
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_1"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_2"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_3"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("FOLDER_1")).perform(click())
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_1")))))).check(matches(isGone()))
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_2")))))).check(matches(isGone()))
        onView(allOf(withId(R.id.btn_move_selected_items), withParent(withChild(withChild(withText("FOLDER_3")))))).check(matches(isGone()))
    }

    @Test
    fun showConfirmDialogOnBreadcrumbClickedInMoveWorkflow() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "FOLDER_1"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_2", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("FOLDER_2")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withTagValue(`is`(R.drawable.ic_home))).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("Möchtest Du 1 Element(e) in den Ordner home verschieben?")).check(matches(isDisplayed()))
    }

    @Test
    fun doNotShowConfirmDialogIfBreadcrumbItemIsNoParentFolder() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "FOLDER_1"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_2", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withTagValue(`is`(R.drawable.ic_home))).perform(click())
        onView(withText("FOLDER_1")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("FOLDER_1")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("FOLDER_2")).perform(longClick())
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("FOLDER_1")).perform(click())
        onView(withId(R.id.fab_cancel_workflow)).perform(click())
    }

    @Test
    fun moveItemsToParentFolder() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "FOLDER_1"))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_2", parentFolder = 1))
        folderDao.insertFolder(AvisioFolder(name = "FOLDER_3", parentFolder = 1))
        boxDao.insertBox(AvisioBox(name = "BOX_1", parentFolder = 1))
        boxDao.insertBox(AvisioBox(name = "BOX_2", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("FOLDER_1")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("FOLDER_2")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("FOLDER_3")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("BOX_1")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("BOX_2")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_move_all)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withTagValue(`is`(R.drawable.ic_home))).perform(click())
        onView(withText(R.string.confirm_dialog_confirm_default)).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withTagValue(`is`(R.drawable.ic_home))).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("FOLDER_1")).check(matches(isDisplayed()))
        onView(withText("FOLDER_2")).check(matches(isDisplayed()))
        onView(withText("FOLDER_3")).check(matches(isDisplayed()))
        onView(withText("BOX_1")).check(matches(isDisplayed()))
        onView(withText("BOX_2")).check(matches(isDisplayed()))
    }

}