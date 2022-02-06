package com.avisio.dashboard.usecase.crud_box.read.list_search

import android.widget.SearchView
import com.avisio.dashboard.usecase.crud_box.read.DashboardItemListAdapter

class SearchQueryListener(private val adapter: DashboardItemListAdapter) : SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        adapter.getFilter().filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        adapter.getFilter().filter(newText)
        return false
    }

}