package com.avisio.dashboard.usecase.crud_box.read.dashboard_item.processor

import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem
import com.avisio.dashboard.usecase.crud_box.read.move_items.BoxListView

class BoxProcessor(private val item: DashboardItem, private val view: BoxListView) : DashboardProcessor() {

    override fun deleteItem() {
        view.getBoxRepository().deleteBox(AvisioBox(id = item.id))
    }

}