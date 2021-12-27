package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.LRThroughOriginModel
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import kotlin.math.pow

class LinearRegressionThroughOrigin(private val points: PointSequence) : Regression<LRThroughOriginModel>() {

    override fun compute(): LRThroughOriginModel {
        val xCoords = points.xCoordinates()
        val yCoords = points.yCoordinates()
        val m = sumXY(xCoords, yCoords) / sumSquaredX(xCoords)
        return LRThroughOriginModel(m)
    }

    private fun sumSquaredX(xCoords: List<Double>): Double {
        return xCoords.map { x -> x.pow(2) }.reduce { x1, x2 -> x1 + x2}
    }

    private fun sumXY(xCoords: List<Double>, yCoords: List<Double>): Double {
        var sumXY = 0.0
        for(i in 0 until points.size()) {
            sumXY += xCoords[i] * yCoords[i]
        }
        return sumXY
    }

}