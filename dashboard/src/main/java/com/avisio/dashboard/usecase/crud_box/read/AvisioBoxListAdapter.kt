package com.avisio.dashboard.usecase.crud_box.read

import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.avisio.dashboard.common.data.model.box.AvisioBox

class AvisioBoxListAdapter(
    diffCallback: DiffUtil.ItemCallback<AvisioBox>,
    private var filteredBoxList: List<AvisioBox>,
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

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(sequence: CharSequence?): FilterResults {
                val constraints = sequence.toString()
                val filterResult =  FilterResults()
                if(constraints.isEmpty()) {
                    filteredBoxList = currentList
                    filterResult.values = filteredBoxList
                    return filterResult
                }
                val filteredList = arrayListOf<AvisioBox>()
                for(listItem in currentList) {
                    if(listItem.name.lowercase().contains(constraints.lowercase())) {
                        filteredList.add(listItem)
                    }
                }
                filterResult.values = filteredBoxList
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredBoxList = results?.values as ArrayList<AvisioBox>
                notifyItemRangeChanged(0, currentList.size)
            }

        }
    }

    interface BoxListOnClickListener {
        fun onClick(index: Int)
    }

}