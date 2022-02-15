package com.avisio.dashboard.usecase.crud_box.read.dashboard_item

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.avisio.dashboard.common.data.model.box.AvisioBox
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.search_resuts.SearchQueryResultDetails

@Entity(primaryKeys = ["id", "type"])
data class DashboardItem(

    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "parentFolder")
    val parentFolder: Long?,

    @ColumnInfo(name = "type")
    val type: DashboardItemType,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "icon")
    val icon: Int,

    @ColumnInfo(name = "selected")
    var selected: Boolean = false,

    @ColumnInfo(name = "searchQueryDetails")
    var searchQueryResults: SearchQueryResultDetails? = null

)