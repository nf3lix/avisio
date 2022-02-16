package com.avisio.dashboard.usecase.crud_box.read.bread_crumb

import com.avisio.dashboard.R
import com.avisio.dashboard.common.ui.breadcrump.BreadCrumbDirectoryAdapter
import com.avisio.dashboard.common.ui.breadcrump.BreadCrumbDirectoryElement
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItem

class DashboardBreadCrumbAdapter : BreadCrumbDirectoryAdapter<DashboardItem>() {

    override fun convert(value: DashboardItem): BreadCrumbDirectoryElement {
        if(DashboardBreadCrumb.isHomeItem(value)) {
            return BreadCrumbDirectoryElement(iconId = R.drawable.ic_home, displayName = value.name)
        }
        return BreadCrumbDirectoryElement(displayName = value.name)
    }

}