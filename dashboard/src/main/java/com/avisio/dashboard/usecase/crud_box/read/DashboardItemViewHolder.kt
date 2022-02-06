package com.avisio.dashboard.usecase.crud_box.read

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardItemViewHolder(
    itemView: View,
    private val listAdapter: DashboardItemListAdapter,
    private val onClickListener: DashboardItemListAdapter.DashboardItemOnClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    companion object {

        fun create(parent: ViewGroup, listAdapter: DashboardItemListAdapter, onClickListener: DashboardItemListAdapter.DashboardItemOnClickListener): DashboardItemViewHolder {
            return DashboardItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.dashboard_list_item, parent, false), listAdapter, onClickListener)
        }

    }

    private val itemTextView: TextView = itemView.findViewById(R.id.dashboard_item_text_view)
    private val itemImageView: ImageView = itemView.findViewById(R.id.dashboard_item_image)

    fun bind(item: DashboardItem) {

        itemView.setOnLongClickListener {
            selectClickedItem()
            return@setOnLongClickListener true
        }

        itemTextView.text = item.name
        itemView.setOnClickListener(this)
        return when(item.type) {
            DashboardItemType.FOLDER -> itemImageView.setImageResource(R.drawable.folder_icon)
            DashboardItemType.BOX -> itemImageView.setImageResource(item.icon)
        }
    }

    private fun selectClickedItem() {
        listAdapter.selectedItemPos = adapterPosition
        if(clickedItemIsSelected()) {
            unselect(listAdapter.currentList[listAdapter.selectedItemPos])
        } else {
            listAdapter.currentList[adapterPosition].selected = true
            onClickListener.onItemSelected(adapterPosition)
        }
        listAdapter.notifyItemChanged(listAdapter.selectedItemPos)
    }

    private fun setSelectedItemToNone() {
        listAdapter.selectedItemPos = DashboardItemListAdapter.NO_ITEM_SELECTED
    }

    private fun clickedItemIsSelected(): Boolean {
        return listAdapter.currentList[adapterPosition].selected
    }

    private fun anyItemIsSelected(): Boolean {
        return listAdapter.selectedItems().isNotEmpty()
    }

    fun select(item: DashboardItem) {
        val colorFromResources = ResourcesCompat.getColor(itemView.resources, R.color.primaryLightColor, null)
        itemView.findViewById<CardView>(R.id.dashboard_item_card_view).setBackgroundColor(colorFromResources)
        item.selected = true
        onClickListener.onItemSelected(adapterPosition)
    }

    fun unselect(item: DashboardItem) {
        val colorFromResources = ResourcesCompat.getColor(itemView.resources, R.color.white, null)
        itemView.findViewById<CardView>(R.id.dashboard_item_card_view).setBackgroundColor(colorFromResources)
        item.selected = false
        onClickListener.onItemUnselected(adapterPosition)
    }

    private fun unselectAll() {
        for(dashboardItem in listAdapter.currentList) {
            unselect(dashboardItem)
        }
    }

    override fun onClick(v: View?) {
        if(anyItemIsSelected() && !clickedItemIsSelected()) {
            listAdapter.selectedItemPos = adapterPosition
            selectClickedItem()
            return
        }
        if(clickedItemIsSelected()) {
            listAdapter.selectedItemPos = adapterPosition
            unselect(listAdapter.currentList[listAdapter.selectedItemPos])
            setSelectedItemToNone()
            listAdapter.notifyItemChanged(listAdapter.selectedItemPos)
        } else {
            GlobalScope.launch {
                unselectAll()
                onClickListener.onClick(adapterPosition)
            }
        }
    }

}