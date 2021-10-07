package com.avisio.dashboard.activity.box_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R

class AvisioBoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        fun create(parent: ViewGroup): AvisioBoxViewHolder {
            val view: View = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.box_list_item, parent, false)
            return AvisioBoxViewHolder(view)
        }

    }

    private val boxTextView: TextView = itemView.findViewById(R.id.box_text_view)

    private fun bind(boxTitle: String) {
        boxTextView.text = boxTitle
    }

}