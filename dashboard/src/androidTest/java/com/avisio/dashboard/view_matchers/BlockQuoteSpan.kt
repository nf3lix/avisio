package com.avisio.dashboard.view_matchers

import android.view.View
import android.widget.EditText
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import io.noties.markwon.core.spans.BlockQuoteSpan
import org.hamcrest.CoreMatchers

class BlockQuoteSpan : ViewAssertion {

    companion object {
        fun hasBlockQuote(): ViewAssertion {
            return BlockQuoteSpan()
        }
    }

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if(noViewFoundException != null) {
            throw noViewFoundException
        }
        if(view !is EditText) {
            throw IllegalStateException()
        }
        ViewMatchers.assertThat("block quote span",
            view.editableText.getSpans(0, view.text.length, BlockQuoteSpan::class.java)[0], CoreMatchers.notNullValue())
    }

}