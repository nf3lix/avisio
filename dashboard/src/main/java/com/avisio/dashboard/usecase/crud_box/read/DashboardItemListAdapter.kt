package com.avisio.dashboard.usecase.crud_box.read

import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class DashboardItemListAdapter(
    diffCallback: DiffUtil.ItemCallback<DashboardItem>,
    private val onClickListener: DashboardItemOnClickListener
) : ListAdapter<DashboardItem, DashboardItemViewHolder>(diffCallback) {

    private var initialList = currentList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardItemViewHolder {
        return DashboardItemViewHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: DashboardItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    class DashboardItemDifference : DiffUtil.ItemCallback<DashboardItem>() {

        override fun areItemsTheSame(oldItem: DashboardItem, newItem: DashboardItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DashboardItem, newItem: DashboardItem): Boolean {
            return false
        }

    }

    fun getDashboardItemById(id: Long): DashboardItem? {
        for(item in currentList) {
            if(item.id == id) {
                return item
            }
        }
        return null
    }

    fun getFilter(): Filter {
        return DashboardItemFilter(this, initialList)
    }

    interface DashboardItemOnClickListener {
        suspend fun onClick(index: Int)
    }

    fun updateList(list: List<DashboardItem>?) {
        super.submitList(list)
        if (list != null) {
            initialList = list
        }
    }

}