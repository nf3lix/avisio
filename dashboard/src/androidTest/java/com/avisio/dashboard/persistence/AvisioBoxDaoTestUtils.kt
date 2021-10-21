package com.avisio.dashboard.persistence

import com.avisio.dashboard.common.data.model.AvisioBox

class AvisioBoxDaoTestUtils {

    companion object {

        fun contentEquals(box1: AvisioBox, box2: AvisioBox): Boolean {
            return box1.createDate.time == box2.createDate.time
                   && box1.name == box2.name && box1.icon.iconId == box2.icon.iconId
        }

    }

}
