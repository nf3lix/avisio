package com.avisio.dashboard.activity.box_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card

class CardViewHolder(
    itemView: View, private val
    onClickListener: CardListAdapter.CardListOnClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    companion object {

        fun create(parent: ViewGroup, onClickListener: CardListAdapter.CardListOnClickListener): CardViewHolder {
            return CardViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.card_list_item, parent, false), onClickListener)
        }

    }

    private val cardTextView: TextView = itemView.findViewById(R.id.card_text_view)

    fun bind(card: Card) {
        cardTextView.text = card.question.toString()
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        onClickListener.onClick(adapterPosition)
    }


}