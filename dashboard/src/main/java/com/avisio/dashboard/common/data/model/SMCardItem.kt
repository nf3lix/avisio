package com.avisio.dashboard.common.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.INTERVAL_BASE
import com.avisio.dashboard.usecase.training.super_memo.model.CardItem
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Card::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("cardId"),
        onDelete = CASCADE
    )]
)
data class SMCardItem(

    @PrimaryKey
    val cardId: Long = 0,

    val lapse: Int = 0,

    val repetition: Int = -1,

    val of: Double = 0.0,

    val af: Double = -1.0,

    val afs: ArrayList<Double> = arrayListOf(),

    var optimumInterval: Double = INTERVAL_BASE.toDouble(),

    val dueDate: Date = Date(0),

    val previousDate: Date = Date(0)

) {

    companion object {

        fun fromCardItem(cardItem: CardItem): SMCardItem {
            val previousDate = if(cardItem.previousDate() == null) Date(0) else cardItem.previousDate()
            return SMCardItem(
                cardId = cardItem.cardId,
                lapse = cardItem.lapse(),
                repetition = cardItem.repetition(),
                of = cardItem.of(),
                af = cardItem.af(),
                afs = cardItem.afs(),
                optimumInterval = cardItem.optimumInterval(),
                dueDate = cardItem.dueDate(),
                previousDate = previousDate!!
            )
        }

    }

}