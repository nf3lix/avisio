package com.avisio.dashboard.usecase.crud_card.common.input_flex_box

import com.avisio.dashboard.R

enum class CardFlexBoxInformationType(val color: Int, val drawable: Int) {
    BLANK(-1, -1),
    SUCCESS(R.color.success, R.drawable.ic_check),
    INFORMATION(R.color.information, R.drawable.ic_information),
    WARNING(R.color.warning, R.drawable.ic_warning),
    ERROR(R.color.error, R.drawable.ic_error)
}