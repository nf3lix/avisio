package com.avisio.dashboard.view_actions

import android.text.style.BackgroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.text.getSpans
import androidx.core.text.toSpannable
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers

class SearchResultMatcher {

    companion object {

        fun isSearchResultHighlighted(start: Int, end: Int): ViewAssertion {
            return SearchResultMatches(start, end)
        }

        fun nonMatchingPartIsNotHighlighted(start: Int, end: Int): ViewAssertion {
            return SearchResultNotMatched(start, end)
        }

    }



    class SearchResultMatches(private val start: Int, private val end: Int): ViewAssertion {
        override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
            if(noViewFoundException != null) {
                throw noViewFoundException
            }
            if(view !is TextView) {
                throw IllegalStateException()
            }
            ViewMatchers.assertThat("background color span",
                view.text.toSpannable().getSpans<BackgroundColorSpan>(start, end).size, CoreMatchers.not(0))
        }

    }

    class SearchResultNotMatched(private val start: Int, private val end: Int): ViewAssertion {
        override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
            if(noViewFoundException != null) {
                throw noViewFoundException
            }
            if(view !is TextView) {
                throw IllegalStateException()
            }
            ViewMatchers.assertThat("background color span",
                view.text.toSpannable().getSpans<BackgroundColorSpan>(start, end).size, CoreMatchers.equalTo(0))
        }

    }

}