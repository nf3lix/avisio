package com.avisio.dashboard.usecase.crud_box.read

import android.widget.Filter
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.search_results.SearchQueryResultDetails

class DashboardItemFilter(
    private val adapter: DashboardItemListAdapter,
    private val initialList: List<DashboardItem>,
    private val allItemsList: List<DashboardItem>) : Filter() {

    private var filteredList: List<DashboardItem> = listOf()
    private var constraints = ""

    override fun performFiltering(sequence: CharSequence?): FilterResults {
        constraints = sequence.toString()
        val filterResult =  FilterResults()
        if(constraints.isEmpty()) {
            filteredList = initialList
            filterResult.values = initialList
            for(items in allItemsList) {
                items.searchQueryResults = null
            }
            return filterResult
        }
        val filteredList = getFilteredList()
        this.filteredList = filteredList
        filterResult.values = filteredList
        return filterResult
    }

    private fun getFilteredList(): List<DashboardItem> {
        for(listItem in initialList) {
            processSearchForCurrentlyDisplayedItem(listItem)
            processSearchForCurrentlyDisplayedFolder(listItem)
        }
        return getMatchedItems()
    }

    private fun getMatchedItems(): List<DashboardItem> {
        val filteredList = arrayListOf<DashboardItem>()
        for(listItem in initialList) {
            if(listItem.searchQueryResults != null) {
                filteredList.add(listItem)
            }
        }
        return filteredList
    }

    private fun processSearchForCurrentlyDisplayedItem(dashboardItem: DashboardItem) {
        if(!dashboardItem.name!!.lowercase().contains(constraints.lowercase())) {
            dashboardItem.searchQueryResults = null
            return
        }
        dashboardItem.searchQueryResults = SearchQueryResultDetails(
            constraints,
            true,
            matchesInFolder = arrayListOf()
        )
    }

    private fun processSearchForCurrentlyDisplayedFolder(dashboardItem: DashboardItem) {
        if(dashboardItem.type == DashboardItemType.FOLDER) {
            val matches = getAllMatchesInNestedFolder(dashboardItem, constraints)
            if(matches.isNotEmpty()) {
                dashboardItem.searchQueryResults = SearchQueryResultDetails(
                    constraints, false, matchesInFolder = matches)
            }
        }
    }

    private fun getAllMatchesInNestedFolder(folder: DashboardItem, constraints: String): List<DashboardItem> {
        val matchesInFolders = arrayListOf<DashboardItem>()
        for(item in allItemsList) {
            if(item.name!!.lowercase().contains(constraints.lowercase()) && itemIsInFolder(item, folder.id)) {
                matchesInFolders.add(item)
            }
        }
        return matchesInFolders
    }

    private fun itemIsInFolder(item: DashboardItem, folderId: Long): Boolean {
        var currentParentFolder = getDirectParentFolder(item)
        while(currentParentFolder != null) {
            if(currentParentFolder.id == folderId) {
                return true
            }
            currentParentFolder = getDirectParentFolder(currentParentFolder)
        }
        return false
    }

    private fun getDirectParentFolder(item: DashboardItem): DashboardItem? {
        for(currentItem in allItemsList) {
            if(currentItem.type == DashboardItemType.FOLDER && currentItem.id == item.parentFolder) {
                return currentItem
            }
        }
        return null
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        filteredList = results?.values as List<DashboardItem>
        adapter.submitList(filteredList)
        adapter.notifyItemRangeChanged(0, filteredList.count())
    }

}