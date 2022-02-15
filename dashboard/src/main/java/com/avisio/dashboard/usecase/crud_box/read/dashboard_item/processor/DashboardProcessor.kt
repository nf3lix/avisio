package com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor

import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import com.avisio.dashboard.usecase.crud_box.read.move_items.BoxListView

abstract class DashboardProcessor {

    companion object {

        fun get(boxListView: BoxListView, item: DashboardItem): DashboardProcessor {
            return when(item.type) {
                DashboardItemType.FOLDER -> FolderProcessor(item, boxListView)
                DashboardItemType.BOX -> BoxProcessor(item, boxListView)
            }
        }

    }

    abstract fun deleteItem()

}