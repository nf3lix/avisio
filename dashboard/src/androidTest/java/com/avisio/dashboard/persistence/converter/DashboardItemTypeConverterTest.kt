package com.avisio.dashboard.persistence.converter

import com.avisio.dashboard.common.data.database.converters.DashboardItemConverter
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import org.junit.Assert
import org.junit.Test

class DashboardItemTypeConverterTest {

    private val converter = DashboardItemConverter()

    @Test
    fun convertItemTypeTest() {
        val box: DashboardItemType = DashboardItemType.BOX
        val folder: DashboardItemType = DashboardItemType.FOLDER
        val boxConverted = converter.enumToInt(box)
        val folderConverted = converter.enumToInt(folder)
        Assert.assertEquals(converter.intToEnum(boxConverted), box)
        Assert.assertEquals(converter.intToEnum(folderConverted), folder)
    }

}