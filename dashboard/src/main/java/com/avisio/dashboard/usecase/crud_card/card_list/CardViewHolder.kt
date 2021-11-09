package com.avisio.dashboard.usecase.crud_card.card_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import java.lang.StringBuilder

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
    private val cardImageView: ImageView = itemView.findViewById(R.id.card_image_View)

    fun bind(card: Card) {
        cardTextView.text = getCardStringRepresentation(card)
        cardImageView.setImageResource(card.type.iconId)
        itemView.setOnClickListener(this)
    }

    private fun getCardStringRepresentation(card: Card): String {
        val sb = StringBuilder()
        for(token in card.question.tokenList) {
            sb.append(token.content + " ")
        }
        return sb.toString()
    }

    override fun onClick(v: View?) {
        onClickListener.onClick(adapterPosition)
    }


}