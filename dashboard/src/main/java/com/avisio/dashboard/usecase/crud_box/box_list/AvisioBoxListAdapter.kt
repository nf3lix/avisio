package com.avisio.dashboard.usecase.crud_box.box_list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.avisio.dashboard.common.data.model.box.AvisioBox

class AvisioBoxListAdapter(
    diffCallback: DiffUtil.ItemCallback<AvisioBox>,
    private val onClickListener: BoxListOnClickListener
) : ListAdapter<AvisioBox, AvisioBoxViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvisioBoxViewHolder {
        return AvisioBoxViewHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: AvisioBoxViewHolder, position: Int) {
        val currentBox = getItem(position)
        holder.bind(currentBox)
    }

    class AvisioBoxDifference : DiffUtil.ItemCallback<AvisioBox>() {

        override fun areItemsTheSame(oldItem: AvisioBox, newItem: AvisioBox): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AvisioBox, newItem: AvisioBox): Boolean {
            return false
        }

    }

    fun getBoxById(id: Long): AvisioBox? {
        for(box in currentList) {
            if(box.id == id) {
                return box
            }
        }
        return null
    }

    interface BoxListOnClickListener {
        fun onClick(index: Int)
    }

}