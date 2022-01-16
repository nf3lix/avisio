package com.avisio.dashboard.common.persistence

import android.app.Application
import com.avisio.dashboard.common.data.database.AppDatabase
import com.avisio.dashboard.usecase.training.super_memo.SuperMemoIntf
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurveEntity
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves

class ForgettingCurveRepository(application: Application) {

    private val dao: ForgettingCurveDao
    private val database: AppDatabase = AppDatabase(application)

    init {
        dao = database.forgettingCurveDao()
    }

    suspend fun getForgettingCurves(): ForgettingCurves {
        val curves = dao.getAll()
        return ForgettingCurves(curves)
    }

    suspend fun createCurves(curves: ForgettingCurves) {
        val c = curves.curves()
        for((i, list) in c.withIndex()) {
            for((j, curve) in list.withIndex()) {
                dao.insert(ForgettingCurveEntity(repetition = i, afIndex = j, curve = curve))
            }
        }
    }

}