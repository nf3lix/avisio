package com.avisio.dashboard.usecase.crud_card.card_list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.avisio.dashboard.common.data.model.card.Card

class CardListAdapter(
    diffCallback: DiffUtil.ItemCallback<Card>,
    private val onClickListener: CardListOnClickListener
) : ListAdapter<Card, CardViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentCard = getItem(position)
        holder.bind(currentCard)
    }

    class CardDifference : DiffUtil.ItemCallback<Card>() {

        override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
            return false
        }

    }

    interface CardListOnClickListener {
        fun onClick(index: Int)
    }

}