package com.avisio.dashboard.view_matchers

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeDiagnosingMatcher

class IsGoneMatcher : TypeSafeDiagnosingMatcher<View>() {

    companion object {

        fun isGone(): IsGoneMatcher {
            return IsGoneMatcher()
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("view is gone")
    }

    override fun matchesSafely(item: View, mismatchDescription: Description?): Boolean {
        return item.visibility == View.GONE
    }

}