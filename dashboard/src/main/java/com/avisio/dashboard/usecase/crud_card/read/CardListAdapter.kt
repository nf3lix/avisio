package com.avisio.dashboard.usecase.crud_card.read

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.usecase.crud_box.read.CardViewHolderItem

class CardListAdapter(
    diffCallback: DiffUtil.ItemCallback<CardViewHolderItem>,
    private val onClickListener: CardListOnClickListener
) : ListAdapter<CardViewHolderItem, CardViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentCard = getItem(position)
        holder.bind(currentCard)
    }

    class CardDifference : DiffUtil.ItemCallback<CardViewHolderItem>() {

        override fun areItemsTheSame(oldItem: CardViewHolderItem, newItem: CardViewHolderItem): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: CardViewHolderItem, newItem: CardViewHolderItem): Boolean {
            return false
        }

    }

    interface CardListOnClickListener {
        fun onClick(index: Int)
    }

}