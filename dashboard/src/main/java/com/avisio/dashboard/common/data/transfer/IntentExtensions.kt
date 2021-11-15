package com.avisio.dashboard.common.data.transfer

import android.content.Intent
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

fun Intent.setBoxObject(box: AvisioBox) {
    val parcelableBox = ParcelableAvisioBox.createFromEntity(box)
    putExtra(IntentKeys.BOX_OBJECT, parcelableBox)
}

fun Intent.getBoxObject(): AvisioBox? {
    val parcelableBox = getParcelableExtra<ParcelableAvisioBox>(IntentKeys.BOX_OBJECT)
        ?: return null
    return ParcelableAvisioBox.createEntity(parcelableBox)
}

fun Intent.setCardObject(card: Card) {
    val parcelableBox = ParcelableCard.createFromEntity(card)
    putExtra(IntentKeys.CARD_OBJECT, parcelableBox)
}

fun Intent.getCardObject(): Card? {
    val parcelableCard = getParcelableExtra<ParcelableCard>(IntentKeys.CARD_OBJECT)
        ?: return null
    return ParcelableCard.createEntity(parcelableCard)
}