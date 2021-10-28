package com.avisio.dashboard.usecase.crud_box.box_activity

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_card.create_card.CreateCardActivity
import com.avisio.dashboard.usecase.crud_box.edit_box.EditBoxActivity
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.persistence.CardDao
import com.avisio.dashboard.usecase.crud_box.box_list.BoxActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoxActivityTest {

    private lateinit var intent: Intent
    private lateinit var cardDao: CardDao
    private lateinit var database: AppDatabase

    init {
        val box = ParcelableAvisioBox(1, "BOX_NAME", R.drawable.box_icon_language)
        intent = Intent(ApplicationProvider.getApplicationContext(), BoxActivity::class.java)
        intent.putExtra(BoxActivity.PARCELABLE_BOX_KEY, box)
    }


    @get:Rule
    val activityScenario = ActivityScenarioRule<BoxActivity>(intent)

    @Before
    fun setup() {
        init()
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = AppDatabase(context)
        cardDao = database.cardDao()
        cardDao.deleteAll()
    }

    @After
    fun releaseIntents() {
        release()
    }

    @Test
    fun startCreateCardActivityOnFabClicked() {
        onView(withId(R.id.fab_new_card)).perform(click())
        intended(hasComponent(CreateCardActivity::class.java.name))
    }

    @Test
    fun showDialogOnDeleteMenuItemClicked() {
        openContextualActionModeOverflowMenu()
        onView(withText(R.string.box_activity_menu_delete)).perform(click())
        onView(withText(R.string.delete_box_confirm_dialog_message)).check(matches(isDisplayed()))
    }

    @Test(expected = NoMatchingViewException::class)
    fun closeDeleteDialogOnCancelClicked() {
        openContextualActionModeOverflowMenu()
        onView(withText(R.string.box_activity_menu_delete)).perform(click())
        onView(withText(R.string.confirm_dialog_cancel_default)).perform(click())
        onView(withText(R.string.delete_box_confirm_dialog_message)).check(matches(isDisplayed()))
    }

    @Test
    fun startEditBoxActivityOnMenuItemSelected() {
        openContextualActionModeOverflowMenu()
        onView(withText(R.string.box_activity_menu_edit)).perform(click())
        intended(hasComponent(EditBoxActivity::class.java.name))
    }

}