package com.avisio.dashboard.usecase.box_list

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
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
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.persistence.AvisioBoxDao
import com.avisio.dashboard.usecase.MainActivity
import com.google.android.material.textview.MaterialTextView
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoxListFragmentTest {

    private var intent: Intent
    private lateinit var boxDao: AvisioBoxDao
    private lateinit var database: AppDatabase

    init {
        val box = ParcelableAvisioBox(1, "BOX_NAME", R.drawable.box_icon_language)
        intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        //intent.putExtra(IntentKeys.BOX_OBJECT, box)
    }


    @get:Rule
    val activityScenario = ActivityScenarioRule<MainActivity>(intent)

    @Before
    fun setup() {
        init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        boxDao = database.boxDao()
        boxDao.deleteAll()
        boxDao.insertBox(AvisioBox(name = "AAA"))
        boxDao.insertBox(AvisioBox(name = "BBB"))
        boxDao.insertBox(AvisioBox(name = "CCC"))
        boxDao.insertBox(AvisioBox(name = "DDD"))
    }

    @After
    fun releaseIntents() {
        boxDao.deleteAll()
        Intents.release()
    }

    @Test
    fun searchViewIsDisplayed() {
        onView(withId(R.id.box_list_search)).check(matches(isDisplayed()))
    }

    @Test
    fun findFilteredItems() {
        onView(withId(R.id.box_list_search)).perform(click())
        typeInSearchView("A")
        onView(allOf(withText("AAA"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        typeInSearchView("A")
        onView(allOf(withText("AAA"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        typeInSearchView("A")
        onView(allOf(withText("AAA"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun hideNonMatchingItems() {
        onView(withId(R.id.box_list_search)).perform(click())
        typeInSearchView("A")
        onView(allOf(withText("BBB"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(not(isDisplayed())))
    }

    @Test
    fun resetFilter() {
        onView(withId(R.id.box_list_search)).perform(click())
        typeInSearchView("A")
        resetSearchView()
        onView(allOf(withText("BBB"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        onView(allOf(withText("CCC"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
        onView(allOf(withText("DDD"), withClassName(`is`(MaterialTextView::class.java.name)))).check(matches(isDisplayed()))
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