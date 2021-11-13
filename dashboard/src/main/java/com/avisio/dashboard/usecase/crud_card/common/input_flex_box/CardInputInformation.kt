package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

class CardInputInformation(val message: String, val type: CardFlexBoxInformationType) {

    companion object {
        val NONE = CardInputInformation("", CardFlexBoxInformationType.BLANK)
    }

    fun isBlank(): Boolean {
        return this == NONE
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CardInputInformation

        if (message != other.message) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

}