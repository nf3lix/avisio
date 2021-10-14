package com.avisio.dashboard.common.ui

import com.avisio.dashboard.R

enum class BoxIcon(val iconId: Int) {

    DEFAULT(R.drawable.box_icon_default),
    LANGUAGE(R.drawable.box_icon_language);

    override fun toString(): String {
        return this.name
    }

}