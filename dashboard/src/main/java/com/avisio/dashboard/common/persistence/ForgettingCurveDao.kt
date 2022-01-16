package com.avisio.dashboard.common.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurveEntity

@Dao
interface ForgettingCurveDao {

    @Query("SELECT * FROM forgettingCurve")
    fun getAll(): List<ForgettingCurveEntity>

    @Update
    fun updateForgettingCurve(curve: ForgettingCurveEntity)

    @Insert
    fun insert(curve: ForgettingCurveEntity)

}