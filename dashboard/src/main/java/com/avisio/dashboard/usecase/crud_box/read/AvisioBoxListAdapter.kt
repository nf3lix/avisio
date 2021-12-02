package com.avisio.dashboard.usecase.crud_box.read

import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.avisio.dashboard.common.data.model.box.AvisioBox

class AvisioBoxListAdapter(
    diffCallback: DiffUtil.ItemCallback<AvisioBox>,
    private val onClickListener: BoxListOnClickListener
) : ListAdapter<AvisioBox, AvisioBoxViewHolder>(diffCallback) {

    private var initialList = currentList

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

    fun getFilter(): Filter {
        return BoxFilter(this, initialList)
    }

    interface BoxListOnClickListener {
        fun onClick(index: Int)
    }

    fun updateList(list: List<AvisioBox>?) {
        super.submitList(list)
        if (list != null) {
            initialList = list
        }
    }

}