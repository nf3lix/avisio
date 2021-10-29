package com.avisio.dashboard.common.data.transfer

import android.os.Bundle
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard

fun Bundle.getCardObject(): Card? {
    val parcelableCard = getParcelable<ParcelableCard>(IntentKeys.CARD_OBJECT)
        ?: return null
    return ParcelableCard.createEntity(parcelableCard)
}

fun Bundle.getBoxObject(): AvisioBox? {
    val parcelableBox = getParcelable<ParcelableAvisioBox>(IntentKeys.BOX_OBJECT)
        ?: return null
    return ParcelableAvisioBox.createEntity(parcelableBox)
}

object IntentKeys {

    const val BOX_OBJECT = "BOX_OBJECT_KEY"
    const val CARD_OBJECT = "CARD_OBJECT_KEY"

}