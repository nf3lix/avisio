package com.avisio.dashboard.usecase.crud_card.read

import androidx.room.Embedded
import androidx.room.Relation
import com.avisio.dashboard.common.data.model.sm.SMCardItem
import com.avisio.dashboard.common.data.model.card.Card

data class CardViewHolderItem(

    @Embedded
    val card: Card,

    @Relation(
        parentColumn = "id",
        entityColumn = "cardId"
    )
    val smCardItem: SMCardItem?

)