package com.avisio.dashboard.usecase.crud_box.read

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardItemViewHolder(
    itemView: View,
    private val onClickListener: DashboardItemListAdapter.DashboardItemOnClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    companion object {

        fun create(parent: ViewGroup, onClickListener: DashboardItemListAdapter.DashboardItemOnClickListener): DashboardItemViewHolder {
            return DashboardItemViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.dashboard_list_item, parent, false), onClickListener)
        }

    }

    private val itemTextView: TextView = itemView.findViewById(R.id.dashboard_item_text_view)
    private val itemImageView: ImageView = itemView.findViewById(R.id.dashboard_item_image)

    fun bind(item: DashboardItem) {
        itemTextView.text = item.name
        itemView.setOnClickListener(this)
        return when(item.type) {
            DashboardItemType.FOLDER -> itemImageView.setImageResource(R.drawable.folder_icon)
            DashboardItemType.BOX -> itemImageView.setImageResource(item.icon)
        }
    }

    override fun onClick(v: View?) {
        GlobalScope.launch {
            onClickListener.onClick(adapterPosition)
        }
    }

}