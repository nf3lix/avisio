package com.avisio.dashboard.activity.box_activity

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    companion object {
    }

    private val cardTextView: TextView = itemView.findViewById(R.id.card_text_view)

    fun bind(card: Card) {
        cardTextView.text = card.question.toString()
        itemView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        // TODO("Not yet implemented")
    }


}