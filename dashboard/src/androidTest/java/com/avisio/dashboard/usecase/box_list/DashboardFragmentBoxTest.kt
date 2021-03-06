package com.avisio.dashboard.usecase.box_list

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.SearchView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.persistence.box.AvisioBoxDao
import com.avisio.dashboard.common.persistence.folder.FolderDao
import com.avisio.dashboard.usecase.MainActivity
import com.avisio.dashboard.view_actions.Wait.Companion.waitFor
import com.avisio.dashboard.view_actions.WaitForView
import com.google.android.material.textview.MaterialTextView
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class DashboardFragmentBoxTest {

    private var intent: Intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
    private lateinit var boxDao: AvisioBoxDao
    private lateinit var folderDao: FolderDao
    private lateinit var database: AppDatabase

    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    @Before
    fun setup() {
        init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        boxDao = database.boxDao()
        folderDao = database.folderDao()
        boxDao.deleteAll()
        boxDao.insertBox(AvisioBox(name = "AAA"))
        boxDao.insertBox(AvisioBox(name = "BBB"))
        boxDao.insertBox(AvisioBox(name = "CCC"))
        boxDao.insertBox(AvisioBox(name = "DDD"))
    }

    @After
    fun releaseIntents() {
        boxDao.deleteAll()
        folderDao.deleteAll()
        Intents.release()
    }

    @Test
    fun searchViewIsDisplayed() {
        onView(isRoot()).perform(WaitForView.withId(R.id.dashboard_list_search, TimeUnit.SECONDS.toMillis(15)))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).check(matches(isDisplayed()))
    }

    @Test
    fun findFilteredItems() {
        onView(isRoot()).perform(WaitForView.withId(R.id.dashboard_list_search, TimeUnit.SECONDS.toMillis(15)))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("A")
        onView(isRoot()).perform(waitFor(200))
        onView(allOf(withText("AAA"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        typeInSearchView("A")
        onView(allOf(withText("AAA"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        typeInSearchView("A")
        onView(allOf(withText("AAA"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun hideNonMatchingItems() {
        onView(isRoot()).perform(WaitForView.withId(R.id.dashboard_list_search, TimeUnit.SECONDS.toMillis(15)))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("A")
        onView(allOf(withText("BBB"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(not(isDisplayed())))
    }

    @Test
    fun resetFilter() {
        onView(isRoot()).perform(WaitForView.withId(R.id.dashboard_list_search, TimeUnit.SECONDS.toMillis(15)))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("A")
        resetSearchView()
        onView(allOf(withText("BBB"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        onView(allOf(withText("CCC"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        onView(allOf(withText("DDD"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
    }

    @Test
    fun resetSearchCriteriaOnBackPressed() {
        onView(isRoot()).perform(WaitForView.withId(R.id.dashboard_list_search, TimeUnit.SECONDS.toMillis(15)))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("A")
        Espresso.pressBack()
        onView(allOf(withText("BBB"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        onView(allOf(withText("CCC"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        onView(allOf(withText("DDD"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun hideSearchAutoCompleteOnBackPressed() {
        onView(isRoot()).perform(WaitForView.withId(R.id.dashboard_list_search, TimeUnit.SECONDS.toMillis(15)))
        onView(isRoot()).perform(waitFor(800))
        onView(withId(R.id.dashboard_list_search)).perform(click())
        typeInSearchView("A")
        Espresso.pressBack()
        Espresso.pressBack()
        onView(withClassName(`is`(SearchView.SearchAutoComplete::class.java.name))).check(matches(not(isDisplayed())))
    }

    private fun typeInSearchView(text: String) {
        onView(withResourceName("search_src_text")).perform(typeText(text))
        closeSoftKeyboard()
    }

    private fun resetSearchView() {
        onView(withResourceName("search_src_text")).perform(clearText())
        closeSoftKeyboard()
    }

}