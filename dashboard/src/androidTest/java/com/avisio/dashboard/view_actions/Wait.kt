package com.avisio.dashboard.view_actions

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matcher

class Wait(private val millis: Long) : ViewAction {

    companion object {
        fun waitFor(millis: Long): Wait {
            return Wait(millis)
        }
    }

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isRoot()
    }

    override fun getDescription(): String {
        return "Wait for $millis milliseconds"
    }

    override fun perform(uiController: UiController, view: View?) {
        uiController.loopMainThreadForAtLeast(millis)
    }
}