package com.avisio.dashboard.common.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import com.google.gson.Gson

@ProvidedTypeConverter
class ForgettingCurveConverter {

    @TypeConverter
    fun forgettingCurveToString(forgettingCurve: ForgettingCurves.ForgettingCurve): String {
        val gson = Gson()
        return gson.toJson(forgettingCurve.points).toString()
    }

    @TypeConverter
    fun stringToForgettingCurve(serialized: String): ForgettingCurves.ForgettingCurve {
        val gson = Gson()
        val sequence = gson.fromJson(serialized, PointSequence::class.java)
        return ForgettingCurves.ForgettingCurve(sequence)
    }

}