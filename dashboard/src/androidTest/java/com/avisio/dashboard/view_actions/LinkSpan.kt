package com.avisio.dashboard.view_actions

import android.view.View
import android.widget.EditText
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.ViewMatchers
import com.avisio.dashboard.usecase.crud_card.common.input_flex_box.markdown.handlers.LinkEditHandler
import org.hamcrest.CoreMatchers

class LinkSpan : ViewAssertion {

    companion object {
        fun hasLinkSpan(): ViewAssertion {
            return LinkSpan()
        }
    }

    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if(noViewFoundException != null) {
            throw noViewFoundException
        }
        if(view !is EditText) {
            throw IllegalStateException()
        }
        ViewMatchers.assertThat("link span",
            view.editableText.getSpans(0, view.text.length, LinkEditHandler.EditLinkSpan::class.java)[0], CoreMatchers.notNullValue())
    }

}