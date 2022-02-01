package com.avisio.dashboard.usecase.crud_card.read

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.avisio.dashboard.R
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.usecase.crud_box.read.CardViewHolderItem
import com.avisio.dashboard.usecase.crud_box.read.due_date.RemainingTime
import kotlin.math.absoluteValue

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
    private val dueDateTextView: TextView = itemView.findViewById(R.id.card_due_date)

    fun bind(card: CardViewHolderItem) {
        cardTextView.text = getCardStringRepresentation(card.card)
        cardImageView.setImageResource(card.card.type.iconId)
        setDueDate(card)
        itemView.setOnClickListener(this)
    }

    private fun setDueDate(card: CardViewHolderItem) {
        if(card.smCardItem?.dueDate == null || card.smCardItem.dueDate.time == 0L) {
            dueDateTextView.text = itemView.context.getString(R.string.card_not_learned_yet)
            dueDateTextView.setTextColor(itemView.context.resources.getColor(R.color.information))
            return
        }
        val remainingTime = RemainingTime(card.smCardItem.dueDate)
        val unitPrefix = itemView.context.resources.getQuantityString(remainingTime.timeUnit.prefixString, remainingTime.remainingTime.toInt().absoluteValue)
        val displayedTextTemplate = if(remainingTime.remainingTime > 0) {
            dueDateTextView.setTextColor(itemView.context.resources.getColor(R.color.success))
            itemView.context.getString(R.string.due_date_in_future_displayed_template, remainingTime.remainingTime.absoluteValue.toString(), unitPrefix)
        } else {
            dueDateTextView.setTextColor(itemView.context.resources.getColor(R.color.error))
            itemView.context.getString(R.string.due_date_in_past_displayed_template, remainingTime.remainingTime.absoluteValue.toString(), unitPrefix)
        }
        dueDateTextView.text = displayedTextTemplate
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