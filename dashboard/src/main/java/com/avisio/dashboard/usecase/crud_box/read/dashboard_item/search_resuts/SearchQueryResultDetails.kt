package com.avisio.dashboard.usecase.crud_box.read.dashboard_item.search_resuts

import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

data class SearchQueryResultDetails(

    val searchQuery: String,
    val rootFolderMatches: Boolean,
    val matchesInFolder: List<DashboardItem>

) {

    fun getMatchedItemNames(): List<String> {
        val matchedNames = arrayListOf<String>()
        for(item in matchesInFolder) {
            matchedNames.add(item.name!!)
        }
        return matchedNames
    }

}