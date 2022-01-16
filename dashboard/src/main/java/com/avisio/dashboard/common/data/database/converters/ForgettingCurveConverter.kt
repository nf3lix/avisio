package com.avisio.dashboard.common.data.database.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

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

    @TypeConverter
    fun afsToString(afs: ArrayList<Double>): String {
        return Gson().toJson(afs)
    }

    @TypeConverter
    fun stringToAfs(serialized: String): ArrayList<Double> {
        val listType = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(serialized, listType)
    }

}