package com.avisio.dashboard.activity.box_list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.avisio.dashboard.common.data.model.AvisioBox

class AvisioBoxListAdapter
    (diffCallback: DiffUtil.ItemCallback<AvisioBox>) : ListAdapter<AvisioBox, AvisioBoxViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvisioBoxViewHolder {
        return AvisioBoxViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AvisioBoxViewHolder, position: Int) {
        val currentBox = getItem(position)
        holder.bind(currentBox.name)
    }

}