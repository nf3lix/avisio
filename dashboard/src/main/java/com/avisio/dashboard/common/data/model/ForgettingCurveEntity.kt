package com.avisio.dashboard.common.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves

@Entity(
    tableName = "forgettingCurve",
    primaryKeys = ["repetition", "afIndex"]
)
data class ForgettingCurveEntity(

    @ColumnInfo(name = "repetition")
    val repetition: Int = 0,

    @ColumnInfo(name = "afIndex")
    val afIndex: Int = 0,

    @ColumnInfo(name = "points")
    val curve: ForgettingCurves.ForgettingCurve

) {

    override fun toString(): String {
        return "ForgettingCurveEntity(repetition=$repetition, afIndex=$afIndex, curve=$curve)"
    }
}