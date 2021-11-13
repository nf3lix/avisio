package com.avisio.dashboard.usecase.crud_box.read

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.box.AvisioBox

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
    private val boxImageView: ImageView = itemView.findViewById(R.id.box_image_View)

    fun bind(box: AvisioBox) {
        boxTextView.text = box.name
        boxImageView.setImageResource(box.icon.iconId)
        itemView.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        onClickListener.onClick(adapterPosition)
    }

}