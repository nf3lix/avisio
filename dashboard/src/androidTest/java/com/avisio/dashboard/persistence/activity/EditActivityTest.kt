package com.avisio.dashboard.persistence.activity

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.avisio.dashboard.R
import com.avisio.dashboard.activity.box_activity.BoxActivity
import com.avisio.dashboard.activity.edit_box.EditBoxActivity
import com.avisio.dashboard.common.data.model.ParcelableAvisioBox
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditActivityTest {

    var intent: Intent = Intent(ApplicationProvider.getApplicationContext(), EditBoxActivity::class.java)

    init {
        intent.putExtra(BoxActivity.PARCELABLE_BOX_KEY, ParcelableAvisioBox(0, "TEST_BOX_1"))
    }

    @get:Rule
    val activityScenario: ActivityScenarioRule<EditBoxActivity> = ActivityScenarioRule<EditBoxActivity>(intent)

    @Before
    fun setup() {
        init()
    }

    @After
    fun releaseIntents() {
        release()
    }

    @Test
    fun startCreateBoxActivityOnFabClicked() {
        Espresso.onView(ViewMatchers.withId(R.id.fab_edit_box)).perform(ViewActions.click())
        intended(IntentMatchers.hasComponent(BoxActivity::class.java.name))
    }

}