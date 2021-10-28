package com.avisio.dashboard.common.ui.edit_box

import com.avisio.dashboard.R

enum class BoxIcon(val iconId: Int) {

    DEFAULT(R.drawable.box_icon_default),
    LANGUAGE(R.drawable.box_icon_language);

    override fun toString(): String {
        return this.name
    }

    companion object {

        fun getBoxIcon(iconId: Int): BoxIcon {
            for(boxIcon in values()) {
                if(boxIcon.iconId == iconId) {
                    return boxIcon
                }
            }
            return DEFAULT
        }

    }

}