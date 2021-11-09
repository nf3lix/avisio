package com.avisio.dashboard.common.data.model.card

import com.avisio.dashboard.R

enum class CardType(val iconId: Int) {
    STANDARD(R.drawable.card_icon_standard),
    CLOZE_TEXT(R.drawable.card_icon_cloze_text),
    CUSTOM(R.drawable.card_icon_custom)
}