package com.avisio.dashboard.view_actions

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.util.HumanReadables
import androidx.test.espresso.util.TreeIterables
import org.hamcrest.Matcher
import org.hamcrest.core.Is
import java.util.concurrent.TimeoutException

class WaitForView(private val viewMatcher: Matcher<View>, private val description: String, private val millis: Long) : ViewAction {

    /* https://stackoverflow.com/questions/21417954/espresso-thread-sleep */

    companion object {

        fun withClassName(className: String, millis: Long): ViewAction {
            return WaitForView(
                ViewMatchers.withClassName(Is.`is`(className)),
                "wait for a specific view with class name <$className> during $millis millis.",
                millis,
            )
        }

        fun withText(text: String, millis: Long): ViewAction {
            return WaitForView(
                ViewMatchers.withText(text),
                "wait for a specific view with text <$text> during $millis millis.",
                millis,
            )
        }

        fun withId(id: Int, millis: Long): ViewAction {
            return WaitForView(
                ViewMatchers.withId(id),
                "wait for a specific view with id <$id> during $millis millis",
                millis
            )
        }

    }

    override fun getConstraints(): Matcher<View> {
        return ViewMatchers.isRoot()
    }

    override fun getDescription(): String {
        return this.description
    }

    override fun perform(uiController: UiController, view: View?) {
        uiController.loopMainThreadUntilIdle()
        val startTime = System.currentTimeMillis()
        val endTime = startTime + millis
        do {
            for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                if (viewMatcher.matches(child)) {
                    return
                }
            }
            uiController.loopMainThreadForAtLeast(50)
        } while (System.currentTimeMillis() < endTime)
        throw PerformException.Builder()
            .withActionDescription(this.description)
            .withViewDescription(HumanReadables.describe(view))
            .withCause(TimeoutException())
            .build()
    }

}