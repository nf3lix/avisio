package com.avisio.dashboard.activity.box_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R

class AvisioBoxViewHolder(
    itemView: View,
    private val onClickListener: AvisioBoxListAdapter.BoxListOnClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    companion object {

        fun create(parent: ViewGroup, onClickListener: AvisioBoxListAdapter.BoxListOnClickListener): AvisioBoxViewHolder {
            return AvisioBoxViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.box_list_item, parent, false), onClickListener)
        }

    }

    private val boxTextView: TextView = itemView.findViewById(R.id.box_text_view)

    fun bind(boxTitle: String) {
        boxTextView.text = boxTitle
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onClickListener.onClick(adapterPosition)
    }

}