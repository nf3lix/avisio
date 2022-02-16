package com.avisio.dashboard.view_matchers

import android.view.View
import android.widget.EditText
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import io.noties.markwon.core.spans.CodeSpan
import org.hamcrest.CoreMatchers

class CodeSpan : ViewAssertion {

    companion object {
        fun hasCodeSpan(): ViewAssertion {
            return CodeSpan()
        }
    }

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if(noViewFoundException != null) {
            throw noViewFoundException
        }
        if(view !is EditText) {
            throw IllegalStateException()
        }
        ViewMatchers.assertThat("code span",
            view.editableText.getSpans(0, view.text.length, CodeSpan::class.java)[0], CoreMatchers.notNullValue())
    }

}