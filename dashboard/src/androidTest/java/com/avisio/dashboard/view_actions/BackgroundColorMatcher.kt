package com.avisio.dashboard.view_actions

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher


class BackgroundColorMatcher(private val resourceId: Int) : TypeSafeMatcher<View>() {

    companion object {
        fun hasBackgroundColor(resourceId: Int): BackgroundColorMatcher {
            return BackgroundColorMatcher(resourceId)
        }
    }

    private var backgroundFromView: Int? = null

    override fun matchesSafely(item: View): Boolean {
        val resources = item.context.resources
        val colorFromResources = ResourcesCompat.getColor(resources, resourceId, null)
        backgroundFromView = (item.background as ColorDrawable).color
        return colorFromResources == backgroundFromView
    }

    override fun describeTo(description: Description) {
        description.appendText("Color did not match $resourceId was $backgroundFromView")
    }

}