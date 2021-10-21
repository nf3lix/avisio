package com.avisio.dashboard.activity.box_activity

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.avisio.dashboard.common.data.model.card.Card

class CardListAdapter(
    diffCallback: DiffUtil.ItemCallback<Card>,
    private val onClickListener: CardListOnClickListener) : ListAdapter<Card, CardViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent, onClickListener)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentCard = getItem(position)
        holder.bind(currentCard)
    }

    interface CardListOnClickListener {
        fun onClick(index: Int)
    }

}