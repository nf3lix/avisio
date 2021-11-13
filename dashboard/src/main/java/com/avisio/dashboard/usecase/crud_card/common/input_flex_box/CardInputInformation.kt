package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

data class CardInputInformation(val message: String, val type: CardFlexBoxInformationType) {

    companion object {
        val NONE = CardInputInformation("", CardFlexBoxInformationType.BLANK)
    }

    fun isBlank(): Boolean {
        return this == NONE
    }

}