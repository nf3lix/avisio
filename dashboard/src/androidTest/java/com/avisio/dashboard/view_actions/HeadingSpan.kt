package com.avisio.dashboard.view_actions

import android.view.View
import android.widget.EditText
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import io.noties.markwon.core.spans.HeadingSpan
import org.hamcrest.CoreMatchers

class HeadingSpan(private val headingLevel: Int) : ViewAssertion {

    companion object {
        fun hasHeadingSpan(headingLevel: Int): ViewAssertion {
            return HeadingSpan(headingLevel)
        }
    }

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if(noViewFoundException != null) {
            throw noViewFoundException
        }
        if(view !is EditText) {
            throw IllegalStateException()
        }
        ViewMatchers.assertThat("heading level",
            view.editableText.getSpans(0, view.text.length, HeadingSpan::class.java)[0].level, CoreMatchers.equalTo(headingLevel))
    }

}