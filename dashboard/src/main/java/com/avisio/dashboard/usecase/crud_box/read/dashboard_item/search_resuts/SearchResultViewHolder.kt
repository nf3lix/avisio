package com.avisio.dashboard.usecase.crud_box.read.dashboard_item.search_resuts

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class SearchResultViewHolder(
    private val searchResultTextView: TextView,
    private val itemTextView: TextView,
    private val subdirectoryIndicator: ImageView) {

    private val color = ResourcesCompat.getColor(itemTextView.resources, R.color.primaryColor, null)
    private lateinit var item: DashboardItem
    private lateinit var searchResults: SearchQueryResultDetails

    fun displaySearchResult(item: DashboardItem) {
        this.item = item
        searchResults = item.searchQueryResults!!
        if (!hasMatchesInChildFolders()) {
            searchResultTextView.visibility = View.GONE
            subdirectoryIndicator.visibility = View.GONE
        } else {
            displayResultsInChildFolders()
            subdirectoryIndicator.visibility = View.VISIBLE
        }
        displayResultInRootFolder()
    }

    fun hideSearchResult(item: DashboardItem) {
        searchResultTextView.visibility = View.GONE
        itemTextView.text = item.name
        subdirectoryIndicator.visibility = View.GONE
    }

    private fun displayResultsInChildFolders() {
        searchResultTextView.visibility = View.VISIBLE
        val parentFolderMatches = SearchResultString(getParentFolderResultsString())
        val regions = findMatchingRegions(searchResults.searchQuery, parentFolderMatches.toString())
        for (i in regions.indices) {
            parentFolderMatches.applyMatches(color, regions)
        }
        searchResultTextView.text = parentFolderMatches
    }

    private fun displayResultInRootFolder() {
        val rootFolderTitle = SearchResultString(item.name)
        val rootFolderMatches = findMatchingRegions(searchResults.searchQuery, item.name!!)
        for (i in rootFolderMatches.indices) {
            rootFolderTitle.applyMatches(color, rootFolderMatches)
        }
        itemTextView.text = rootFolderTitle
    }

    private fun findMatchingRegions(constraint: String, content: String): List<SearchResultRegionMatch> {
        val list = arrayListOf<SearchResultRegionMatch>()
        for (i in 0..content.length - constraint.length) {
            val substring = content.substring(i, i + constraint.length)
            if (substring.contentEquals(constraint)) {
                list.add(SearchResultRegionMatch(i, i + constraint.length))
            }
        }
        return list
    }

    private fun hasMatchesInChildFolders(): Boolean {
        return searchResults.matchesInFolder.isNotEmpty()
    }

    private fun getParentFolderResultsString(): String {
        val listString = searchResults.getMatchedItemNames().toString()
        return listString.substring(1, listString.length - 1)
    }

}