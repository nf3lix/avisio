package com.avisio.dashboard.usecase.training.super_memo.model

import androidx.room.ColumnInfo
import androidx.room.Entity

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

)