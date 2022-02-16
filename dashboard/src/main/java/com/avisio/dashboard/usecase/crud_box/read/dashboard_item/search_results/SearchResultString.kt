package com.avisio.dashboard.usecase.crud_box.read.dashboard_item.search_results

import android.text.SpannableString
import android.text.style.BackgroundColorSpan

class SearchResultString(content: String?) : SpannableString(content) {

    fun applyMatches(color: Int, regionMatches: List<SearchResultRegionMatch>) {
        for(match in regionMatches) {
            setSpan(BackgroundColorSpan(color), match.start, match.end, SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

}