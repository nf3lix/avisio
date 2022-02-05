package com.avisio.dashboard.usecase.crud_box.read

import android.widget.Filter
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class DashboardItemFilter(private val adapter: DashboardItemListAdapter, private val initialList: List<DashboardItem>) : Filter() {

    private var filteredList: List<DashboardItem> = listOf()

    override fun performFiltering(sequence: CharSequence?): FilterResults {
        val constraints = sequence.toString()
        val filterResult =  FilterResults()
        if(constraints.isEmpty()) {
            filteredList = initialList
            filterResult.values = initialList
            return filterResult
        }
        val filteredList = getFilteredList(constraints)
        this.filteredList = filteredList
        filterResult.values = filteredList
        return filterResult
    }

    private fun getFilteredList(constraints: String): List<DashboardItem> {
        val filteredList = arrayListOf<DashboardItem>()
        for(listItem in initialList) {
            if(listItem.name!!.lowercase().contains(constraints.lowercase())) {
                filteredList.add(listItem)
            }
        }
        return filteredList
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        filteredList = results?.values as List<DashboardItem>
        adapter.submitList(filteredList)
        adapter.notifyItemRangeChanged(0, filteredList.count())
    }

}