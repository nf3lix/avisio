package com.avisio.dashboard.common.ui.edit_card.input_flex_box

import com.avisio.dashboard.R

enum class CardFlexBoxInformationType(val color: Int, val drawable: Int) {
    SUCCESS(R.color.success, R.drawable.ic_check),
    INFORMATION(R.color.information, R.drawable.ic_information),
    WARNING(R.color.warning, R.drawable.ic_warning),
    ERROR(R.color.error, R.drawable.ic_error)
}