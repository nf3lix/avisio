package com.avisio.dashboard.usecase.box_list

import android.content.Context
import android.content.Intent
import androidx.cardview.widget.CardView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.*
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
import com.avisio.dashboard.view_matchers.BackgroundColorMatcher.Companion.hasBackgroundColor
import com.avisio.dashboard.view_matchers.IsGoneMatcher.Companion.isGone
import com.avisio.dashboard.view_actions.Wait.Companion.waitFor
import com.google.android.material.textview.MaterialTextView
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardFragmentSelectionTest {

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
        database.clearAllTables()
        folderDao = database.folderDao()
        boxDao = database.boxDao()
        boxDao.deleteAll()
        folderDao.deleteAll()
    }

    @After
    fun releaseIntents() {
        database.clearAllTables()
        boxDao.deleteAll()
        folderDao.deleteAll()
        Intents.release()
    }

    @Test
    fun selectSingleFolderOnLongClick() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(800))
        itemIsSelected("F_1")
    }

    @Test
    fun selectSingleBoxOnLongClick() {
        boxDao.insertBox(AvisioBox(name = "B_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("B_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        itemIsSelected("B_1")
    }

    @Test
    fun selectMultipleItemsByLongClick() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2"))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "F_3"))

        onView(isRoot()).perform(waitFor(800))

        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        itemIsSelected("F_1")
        itemIsUnselected("F_2")
        itemIsUnselected("F_3")

        onView(withText("F_2")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        itemIsSelected("F_1")
        itemIsSelected("F_2")
        itemIsUnselected("F_3")

        onView(withText("F_3")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        itemIsSelected("F_1")
        itemIsSelected("F_2")
        itemIsSelected("F_3")

        database.clearAllTables()
        boxDao.deleteAll()
        folderDao.deleteAll()
    }

    @Test
    fun selectMultipleItemsByShortClick() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2"))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "F_3"))

        onView(isRoot()).perform(waitFor(1000))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        itemIsSelected("F_1")
        itemIsUnselected("F_2")
        itemIsUnselected("F_3")

        onView(withText("F_2")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        itemIsSelected("F_1")
        itemIsSelected("F_2")
        itemIsUnselected("F_3")

        onView(withText("F_3")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        itemIsSelected("F_1")
        itemIsSelected("F_2")
        itemIsSelected("F_3")
    }

    @Test
    fun unselectItemsByShortClick() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2"))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "F_3"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("F_2")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("F_3")).perform(longClick())
        onView(isRoot()).perform(waitFor(200))

        onView(withText("F_1")).perform(click())
        onView(isRoot()).perform(waitFor(800))
        itemIsUnselected("F_1")
        itemIsSelected("F_2")
        itemIsSelected("F_3")

        onView(withText("F_2")).perform(click())
        onView(isRoot()).perform(waitFor(800))
        itemIsUnselected("F_1")
        itemIsUnselected("F_2")
        itemIsSelected("F_3")

        onView(withText("F_3")).perform(click())
        onView(isRoot()).perform(waitFor(800))
        itemIsUnselected("F_1")
        itemIsUnselected("F_2")
        itemIsUnselected("F_2")
    }

    @Test(expected = NoMatchingViewException::class)
    fun leaveSelectModeOnUnselectLastSelectedItem() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2"))
        onView(isRoot()).perform(waitFor(200))
        onView(allOf(withText("F_1"), withClassName(`is`(MaterialTextView::class.java.name)))).perform(longClick())
        onView(isRoot()).perform(waitFor(200))
        onView(allOf(withText("F_2"), withClassName(`is`(MaterialTextView::class.java.name)))).perform(longClick())
        onView(isRoot()).perform(waitFor(200))

        onView(allOf(withText("F_1"), withClassName(`is`(MaterialTextView::class.java.name)))).perform(click())
        onView(isRoot()).perform(waitFor(200))

        onView(allOf(withText("F_2"), withClassName(`is`(MaterialTextView::class.java.name)))).perform(click())
        onView(isRoot()).perform(waitFor(200))

        onView(withText("F_1")).perform(click())
        onView(allOf(withText("F_1"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
    }

    @Test
    fun hideUnexpandedFabMenuOnItemSelected() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(100))
        onView(withId(R.id.fab_expand)).check(matches(isGone()))
    }

    @Test
    fun hideExpandedFabMenuOnItemSelected() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        onView(withId(R.id.fab_expand)).perform(click())
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(100))
        onView(withId(R.id.fab_expand)).check(matches(isGone()))
        onView(withId(R.id.fab_new_folder)).check(matches(isGone()))
        onView(withId(R.id.fab_new_box)).check(matches(isGone()))
    }

    @Test
    fun showExpandFabMenuButtonOnUnselectLastSelectedItem() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(100))
        onView(withText("F_1")).perform(click())
        onView(withId(R.id.fab_expand)).check(matches(isDisplayed()))
    }

    @Test
    fun showSelectedItemsActionButtonsOnSelectItem() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(100))
        onView(withId(R.id.btn_delete_all)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_move_all)).check(matches(isDisplayed()))
    }

    @Test
    fun hideSelectedItemsActionButtonsOnUnselectLastSelectedItem() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(100))
        onView(withText("F_1")).perform(click())
        onView(withId(R.id.btn_delete_all)).check(matches(isGone()))
        onView(withId(R.id.btn_move_all)).check(matches(isGone()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun deleteAllSelectedItemsOnActionButtonClicked() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        boxDao.insertBox(AvisioBox(name = "B_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(withText("B_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(100))
        onView(withId(R.id.btn_delete_all)).perform(click())
        onView(withText(R.string.confirm_dialog_confirm_default)).perform(click())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).check(matches(isDisplayed()))
    }

    @Test
    fun keepUnselectedItemsOnDeleteClicked() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        boxDao.insertBox(AvisioBox(name = "B_1"))
        onView(isRoot()).perform(waitFor(1000))
        onView(withText("B_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.btn_delete_all)).perform(click())
        onView(withText(R.string.confirm_dialog_confirm_default)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withText("F_1")).check(matches(isDisplayed()))
    }

    @Test
    fun showFabMenuOnDeleteClicked() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        boxDao.insertBox(AvisioBox(name = "B_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("B_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.btn_delete_all)).perform(click())
        onView(withText(R.string.confirm_dialog_confirm_default)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withId(R.id.fab_expand)).check(matches(isDisplayed()))
    }

    @Test
    fun cancelDeletionOnCancelButtonClicked() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        boxDao.insertBox(AvisioBox(name = "B_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("B_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.btn_delete_all)).perform(click())
        onView(withText(R.string.confirm_dialog_cancel_default)).perform(click())
        onView(isRoot()).perform(waitFor(1000))
        onView(withText("F_1")).check(matches(isDisplayed()))
        onView(withText("B_1")).check(matches(isDisplayed()))
    }

    @Test
    fun showEditButtonIfOneItemIsSelected() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        boxDao.insertBox(AvisioBox(name = "B_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("B_1")).perform(longClick())
        onView(withId(R.id.btn_edit_item)).check(matches(isDisplayed()))
    }

    @Test
    fun hideEditButtonOnUnselectItem() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_edit_item)).check(matches(isGone()))
    }

    @Test
    fun hideEditButtonIfMoreThanOneItemIsSelected() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "F_2"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_2")).perform(click())
        onView(isRoot()).perform(waitFor(200))
        onView(withId(R.id.btn_edit_item)).check(matches(isGone()))
    }

    @Test
    fun editFolderOnEditItemActionButtonClicked() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "F_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("F_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.btn_edit_item)).perform(click())
        onView(withId(R.id.folder_name_edit_text)).perform(typeText("_1"))
        onView(withId(R.id.fab_edit_folder)).perform(click())
        onView(withText("F_1_1")).check(matches(isDisplayed()))
    }

    @Test
    fun editBoxOnEditItemActionButtonClicked() {
        boxDao.insertBox(AvisioBox(name = "B_1"))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("B_1")).perform(longClick())
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.btn_edit_item)).perform(click())
        onView(withId(R.id.box_name_edit_text)).perform(typeText("_1"))
        onView(withId(R.id.fab_edit_box)).perform(click())
        onView(withText("B_1_1")).check(matches(isDisplayed()))
        onView(withContentDescription("More options")).check(matches(isDisplayed()))
    }

    private fun itemIsSelected(name: String) {
        //onView(allOf(withChild(withChild(withChild(withText(name)))), withClassName(`is`(CardView::class.java.name))))
        onView(allOf(withChild(withChild(withChild(withText(name)))), withClassName(`is`(CardView::class.java.name)))).check(matches(hasBackgroundColor(R.color.primaryLightColor)))
    }

    private fun itemIsUnselected(name: String) {
        onView(allOf(withChild(withChild(withChild(withText(name)))), withClassName(`is`(CardView::class.java.name)))).check(matches(hasBackgroundColor(R.color.white)))
    }

}