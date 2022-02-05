package com.avisio.dashboard.common.data.transfer

import android.content.Intent
import android.os.Bundle
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableAvisioBox
import com.avisio.dashboard.common.data.model.box.ParcelableDashboardItem
import com.avisio.dashboard.common.data.model.card.Card
import com.avisio.dashboard.common.data.model.card.parcelable.ParcelableCard
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType

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

fun Intent.setCurrentFolder(item: DashboardItem?) {
    val parcelableItem = if(item == null) {
        ParcelableDashboardItem.createFromEntity(DashboardItem(id = -1, -1, DashboardItemType.FOLDER, "none", -1))
    } else {
        ParcelableDashboardItem.createFromEntity(item)
    }
    putExtra(IntentKeys.DASHBOARD_ITEM, parcelableItem)
}

fun Bundle.setCurrentFolder(item: DashboardItem?) {
    val parcelableItem = if(item == null) {
        ParcelableDashboardItem.createFromEntity(DashboardItem(id = -1, -1, DashboardItemType.FOLDER, "none", -1))
    } else {
        ParcelableDashboardItem.createFromEntity(item)
    }
    putParcelable(IntentKeys.DASHBOARD_ITEM, parcelableItem)
}

fun Bundle.getCurrentFolder(): DashboardItem? {
    val parcelableDashboardItem = getParcelable<ParcelableDashboardItem>(IntentKeys.DASHBOARD_ITEM)
        ?: return null
    return ParcelableDashboardItem.createEntity(parcelableDashboardItem)
}

fun Intent.getCurrentFolder(): DashboardItem? {
    val parcelableDashboardItem = getParcelableExtra<ParcelableDashboardItem>(IntentKeys.DASHBOARD_ITEM)
        ?: return null
    return ParcelableDashboardItem.createEntity(parcelableDashboardItem)
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