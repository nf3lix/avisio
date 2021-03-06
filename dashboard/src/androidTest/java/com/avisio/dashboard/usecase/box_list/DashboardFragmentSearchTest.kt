package com.avisio.dashboard.usecase.box_list

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.*
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
import com.avisio.dashboard.view_matchers.IsGoneMatcher.Companion.isGone
import com.avisio.dashboard.view_matchers.SearchResultMatcher.Companion.isSearchResultHighlighted
import com.avisio.dashboard.view_matchers.SearchResultMatcher.Companion.nonMatchingPartIsNotHighlighted
import com.avisio.dashboard.view_actions.Wait.Companion.waitFor
import com.google.android.material.textview.MaterialTextView
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardFragmentSearchTest {

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
    fun highlightMatchesInSingleItemInCurrentFolder() {
        folderDao.insertFolder(AvisioFolder(name = "AAA111"))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("A")
        onView(withText("AAA111")).check(isSearchResultHighlighted(0, 2))
        onView(withText("AAA111")).check(nonMatchingPartIsNotHighlighted(3, 5))
        typeInSearchView("1")
        onView(withText("AAA111")).check(isSearchResultHighlighted(2, 4))
        onView(withText("AAA111")).check(nonMatchingPartIsNotHighlighted(0, 1))
        onView(withText("AAA111")).check(nonMatchingPartIsNotHighlighted(5, 5))
        typeInSearchView("1")
        onView(withText("AAA111")).check(isSearchResultHighlighted(3, 5))
        onView(withText("AAA111")).check(nonMatchingPartIsNotHighlighted(0, 2))
        typeInSearchView("1")
        onView(withText("AAA111")).check(isSearchResultHighlighted(3, 6))
        onView(withText("AAA111")).check(nonMatchingPartIsNotHighlighted(0, 2))
    }

    @Test
    fun highlightMatchesInMultipleItemsInCurrentFolder() {
        folderDao.insertFolder(AvisioFolder(name = "AA11AA"))
        boxDao.insertBox(AvisioBox(name = "BB11BB"))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("1")
        onView(withText("AA11AA")).check(isSearchResultHighlighted(2, 3))
        onView(withText("BB11BB")).check(isSearchResultHighlighted(2, 3))
        typeInSearchView("1")
        onView(withText("AA11AA")).check(isSearchResultHighlighted(2, 3))
        onView(withText("BB11BB")).check(isSearchResultHighlighted(2, 3))
    }

    @Test
    fun highlightMatchesInSingleChildItemsIfParentIsNotMatched() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "AAA"))
        boxDao.insertBox(AvisioBox(name = "BBB111CCC", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("B")
        onView(withText("BBB111CCC")).check(isSearchResultHighlighted(0, 2))
        onView(withText("BBB111CCC")).check(nonMatchingPartIsNotHighlighted(3, 8))
        onView(withText("AAA")).check(nonMatchingPartIsNotHighlighted(0, 2))
        typeInSearchView("1")
        onView(withText("BBB111CCC")).check(isSearchResultHighlighted(2, 3))
        onView(withText("BBB111CCC")).check(nonMatchingPartIsNotHighlighted(0, 1))
        onView(withText("BBB111CCC")).check(nonMatchingPartIsNotHighlighted(4, 8))
        onView(withText("AAA")).check(nonMatchingPartIsNotHighlighted(0, 2))
        typeInSearchView("11C")
        onView(withText("BBB111CCC")).check(isSearchResultHighlighted(2, 6))
        onView(withText("BBB111CCC")).check(nonMatchingPartIsNotHighlighted(0, 1))
        onView(withText("BBB111CCC")).check(nonMatchingPartIsNotHighlighted(7, 8))
        onView(withText("AAA")).check(nonMatchingPartIsNotHighlighted(0, 2))
    }

    @Test
    fun highlightMatchesInMultipleChildItemsIfParentIsNotMatched() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "AAA"))
        boxDao.insertBox(AvisioBox(name = "B2", parentFolder = 1))
        boxDao.insertBox(AvisioBox(name = "B3", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("B")
        onView(withText("B2, B3")).check(isSearchResultHighlighted(0, 0))
        onView(withText("B2, B3")).check(isSearchResultHighlighted(4, 4))
        onView(withText("B2, B3")).check(nonMatchingPartIsNotHighlighted(1, 3))
        onView(withText("B2, B3")).check(nonMatchingPartIsNotHighlighted(6, 6))
        onView(withText("AAA")).check(nonMatchingPartIsNotHighlighted(0, 2))
        typeInSearchView("2")
        onView(allOf(withText("B2"), withClassName(`is`(MaterialTextView::class.java.name)))).check(isSearchResultHighlighted(0, 1))
    }

    @Test
    fun highlightMatchesInSingleChildItemsIfParentIsMatched() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "AAA"))
        boxDao.insertBox(AvisioBox(name = "AAA111", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("A")
        onView(withText("AAA")).check(isSearchResultHighlighted(0, 2))
        onView(withText("AAA111")).check(isSearchResultHighlighted(0, 2))
        typeInSearchView("AA")
        onView(allOf(withText("AAA"), withClassName(`is`(MaterialTextView::class.java.name)))).check(isSearchResultHighlighted(0, 3))
        onView(withText("AAA111")).check(isSearchResultHighlighted(0, 3))
        typeInSearchView("1")
        onView(withText("AAA111")).check(isSearchResultHighlighted(0, 4))
        onView(withText("AAA")).check(nonMatchingPartIsNotHighlighted(0, 2))
    }

    @Test
    fun highlightMatchesInMultipleChildItemsIfParentIsMatched() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "BB"))
        boxDao.insertBox(AvisioBox(name = "BB2", parentFolder = 1))
        boxDao.insertBox(AvisioBox(name = "BB3", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("B")
        onView(withText("BB2, BB3")).check(isSearchResultHighlighted(0, 1))
        onView(withText("BB2, BB3")).check(isSearchResultHighlighted(5, 6))
        onView(withText("BB2, BB3")).check(nonMatchingPartIsNotHighlighted(2, 4))
        onView(withText("BB2, BB3")).check(nonMatchingPartIsNotHighlighted(2, 4))
        onView(withText("BB")).check(isSearchResultHighlighted(0, 1))
        typeInSearchView("2")
        onView(withText("BB2")).check(isSearchResultHighlighted(1, 2))
    }

    @Test
    fun highlightMatchesFromNestedFolders() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "BB"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "BB2", parentFolder = 1))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "BB3", parentFolder = 2))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("B")
        onView(withText("BB2, BB3")).check(isSearchResultHighlighted(0, 1))
        onView(withText("BB2, BB3")).check(isSearchResultHighlighted(5, 6))
        onView(withText("BB2, BB3")).check(nonMatchingPartIsNotHighlighted(2, 4))
        onView(withText("BB2, BB3")).check(nonMatchingPartIsNotHighlighted(2, 4))
        onView(withText("BB")).check(isSearchResultHighlighted(0, 1))
        typeInSearchView("2")
        onView(withText("BB2")).check(isSearchResultHighlighted(1, 2))
    }

    @Test
    fun resetHighlightingOfItemInCurrentFolder() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "B2"))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        onView(isRoot()).perform(waitFor(800))
        typeInSearchView("B")
        onView(withText("B2")).check(isSearchResultHighlighted(0, 1))
        onView(withText("B")).perform(clearText())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("B2")).check(nonMatchingPartIsNotHighlighted(0, 1))
    }

    @Test(expected = NoMatchingViewException::class)
    fun doNotFindRootFolderAsSearchResultIfCurrentFolderIsChildFolder() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "A"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "BB2", parentFolder = 1))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "BB3", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("A")).perform(click())
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("B")
        onView(withText("BB2")).check(isSearchResultHighlighted(0, 1))
        onView(withText("BB3")).check(isSearchResultHighlighted(0, 1))
        onView(allOf(withText("A"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(not(isDisplayed())))
    }

    @Test
    fun showSubDirectoryResultIndicator() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "AAA"))
        boxDao.insertBox(AvisioBox(name = "B2", parentFolder = 1))
        boxDao.insertBox(AvisioBox(name = "B3", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("B")
        onView(withId(R.id.sub_directory_indicator)).check(matches(isDisplayed()))
    }

    @Test
    fun hideSubDirectoryResultIndicator() {
        onView(isRoot()).perform(waitFor(800))
        folderDao.insertFolder(AvisioFolder(id = 1, name = "AAA"))
        boxDao.insertBox(AvisioBox(name = "B2", parentFolder = 1))
        boxDao.insertBox(AvisioBox(name = "B3", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("B")
        onView(withText("B")).perform(clearText())
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.sub_directory_indicator)).check(matches(isGone()))
    }

    @Test
    fun continueSearchOnChildFolderOpened() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "A1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "A2", parentFolder = 1))
        folderDao.insertFolder(AvisioFolder(id = 3, name = "B2", parentFolder = 1))
        folderDao.insertFolder(AvisioFolder(id = 4, name = "A3", parentFolder = 2))
        folderDao.insertFolder(AvisioFolder(id = 5, name = "B3", parentFolder = 2))

        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("A")

        onView(allOf(withClassName(`is`(MaterialTextView::class.java.name)), withText("A1"))).perform(click())
        onView(isRoot()).perform(waitFor(800))
        onView(allOf(withText("A3"), withClassName(`is`(MaterialTextView::class.java.name)))).check(isSearchResultHighlighted(0, 1))

        onView(allOf(withClassName(`is`(MaterialTextView::class.java.name)), withText("A2"))).perform(click())
        onView(isRoot()).perform(waitFor(800))
        onView(allOf(withText("A3"), withClassName(`is`(MaterialTextView::class.java.name)))).check(isSearchResultHighlighted(0, 1))
    }

    @Test
    fun continueSearchOnParentFolderOpened() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "TEST_1"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "TEST_2", parentFolder = 1))
        onView(isRoot()).perform(waitFor(800))
        onView(withText("TEST_1")).perform(click())
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("TEST")
        onView(withTagValue(`is`(R.drawable.ic_home))).perform(click())
        onView(isRoot()).perform(waitFor(800))
        onView(withText("TEST_1")).check(isSearchResultHighlighted(0, 3))
        onView(withText("TEST_2")).check(isSearchResultHighlighted(0, 3))
    }

    @Test
    fun displayNoMatchingItemHint() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "AAA"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "BBB"))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("C")
        onView(withText(R.string.box_list_no_matching_item_hint)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun removeNotMatchingItemHint() {
        folderDao.insertFolder(AvisioFolder(id = 1, name = "AAA"))
        folderDao.insertFolder(AvisioFolder(id = 2, name = "BBB"))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("C")
        onView(withText(R.string.box_list_no_matching_item_hint)).check(matches(isDisplayed()))
        onView(withResourceName("search_src_text")).perform(clearText())
        onView(isRoot()).perform(waitFor(200))
        onView(withText("AAA")).check(matches(isDisplayed()))
        onView(withText(R.string.box_list_no_matching_item_hint)).check(matches(isDisplayed()))
    }

    private fun typeInSearchView(text: String) {
        onView(withResourceName("search_src_text"))
            .perform(typeText(text))
        Espresso.closeSoftKeyboard()
    }


}