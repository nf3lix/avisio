package com.avisio.dashboard.view_actions

import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class IndexMatcher(private val matcher: Matcher<View>, private val index: Int) : TypeSafeMatcher<View>() {

    private var currentIndex = 0

    override fun describeTo(description: Description?) {
        description?.appendText("with index: ");
        description?.appendValue(index);
        matcher.describeTo(description);
    }

    override fun matchesSafely(item: View?): Boolean {
        return matcher.matches(item) && currentIndex++ == index;
    }
}