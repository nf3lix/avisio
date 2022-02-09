package com.avisio.dashboard.common.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.DashboardItemType
import com.avisio.dashboard.usecase.crud_box.read.dashboard_item.search_resuts.SearchQueryResultDetails
import com.google.gson.Gson

@ProvidedTypeConverter
class DashboardItemConverter {

    @TypeConverter
    fun enumToInt(type: DashboardItemType): Int {
        return type.ordinal
    }

    @TypeConverter
    fun intToEnum(ordinal: Int): DashboardItemType {
        return DashboardItemType.values()[ordinal]
    }

    @TypeConverter
    fun intToSelectedBoolean(selected: Int): Boolean {
        return when(selected) {
            1 -> true
            else -> false
        }
    }

    @TypeConverter
    fun searchQueryResultsToString(searchQuery: SearchQueryResultDetails): String {
        val gson = Gson()
        return gson.toJson(searchQuery).toString()
    }

    @TypeConverter
    fun stringToSearchQueryResults(serializedObject: String): SearchQueryResultDetails {
        val gson = Gson()
        return gson.fromJson(serializedObject, SearchQueryResultDetails::class.java)
    }
}