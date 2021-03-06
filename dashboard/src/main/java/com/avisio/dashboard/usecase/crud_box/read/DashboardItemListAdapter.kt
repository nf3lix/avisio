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

    companion object {
        const val NO_ITEM_SELECTED: Int = -1
    }

    var initialList = currentList
    var selectedItemPos = NO_ITEM_SELECTED
    var moveWorkflowActive = false
    private var allItems = listOf<DashboardItem>()
    val itemFilter = DashboardItemFilter(this, initialList, allItems)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardItemViewHolder {
        return DashboardItemViewHolder.create(parent, this, onClickListener)
    }

    override fun onBindViewHolder(holder: DashboardItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if(currentItem.selected) {
            holder.select(currentList[position])
        }
        holder.bind(currentItem)
    }

    fun selectedItems(): List<DashboardItem> {
        val selectedItems = arrayListOf<DashboardItem>()
        for(dashboardItem in currentList) {
            if(dashboardItem.selected) selectedItems.add(dashboardItem)
        }
        return selectedItems
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

    fun getFilter(): DashboardItemFilter {
        itemFilter.updateItemLists(initialList, allItems)
        return itemFilter
    }

    interface DashboardItemOnClickListener {
        suspend fun onClick(index: Int)
        fun onItemSelected(position: Int)
        fun onItemUnselected(position: Int)
        fun onMoveItemsToFolderClicked(adapterPosition: Int, item: DashboardItem, selectedItems: List<DashboardItem>)
    }

    fun updateList(list: List<DashboardItem>?) {
        super.submitList(list)
        if (list != null) {
            initialList = list
        }
    }

    fun updateAllItemList(list: List<DashboardItem>) {
        allItems = list
    }

}