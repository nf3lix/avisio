package com.avisio.dashboard.common.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.avisio.dashboard.common.ui.edit_box.BoxIcon

@ProvidedTypeConverter
class BoxIconConverter {

    @TypeConverter
    fun boxToInt(boxIcon: BoxIcon): Int {
        return boxIcon.iconId
    }

    @TypeConverter
    fun toBox(boxId: Int): BoxIcon {
        return BoxIcon.getBoxIcon(boxId)
    }

}