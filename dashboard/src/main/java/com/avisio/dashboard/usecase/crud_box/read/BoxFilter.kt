package com.avisio.dashboard.usecase.crud_box.read

import android.widget.Filter
import com.avisio.dashboard.common.data.model.box.AvisioBox

class BoxFilter(private val adapter: AvisioBoxListAdapter, private val initialList: List<AvisioBox>) : Filter() {

    private var filteredBoxList: List<AvisioBox> = listOf()

    override fun performFiltering(sequence: CharSequence?): FilterResults {
        val constraints = sequence.toString()
        val filterResult =  FilterResults()
        if(constraints.isEmpty()) {
            filteredBoxList = initialList
            filterResult.values = initialList
            return filterResult
        }
        val filteredList = getFilteredList(constraints)
        filteredBoxList = filteredList
        filterResult.values = filteredList
        return filterResult
    }

    private fun getFilteredList(constraints: String): List<AvisioBox> {
        val filteredList = arrayListOf<AvisioBox>()
        for(listItem in initialList) {
            if(listItem.name.lowercase().contains(constraints.lowercase())) {
                filteredList.add(listItem)
            }
        }
        return filteredList
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        filteredBoxList = results?.values as List<AvisioBox>
        adapter.submitList(filteredBoxList)
        adapter.notifyItemRangeChanged(0, filteredBoxList.count())
    }

}