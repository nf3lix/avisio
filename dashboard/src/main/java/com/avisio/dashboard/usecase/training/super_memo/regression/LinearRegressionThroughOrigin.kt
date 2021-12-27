package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.LRThroughOriginModel
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence

class LinearRegressionThroughOrigin(private val points: PointSequence) : Regression<LRThroughOriginModel>() {

    override fun compute(): LRThroughOriginModel {
        val m = points.sumXY() / sumSquaredX()
        return LRThroughOriginModel(m)
    }

    private fun sumSquaredX(): Double {
        val squared = points.squaredXCoordinates()
        return xCoordinatesSum(squared)
    }

    private fun xCoordinatesSum(xCoords: ArrayList<Double>): Double {
        var sum = 0.0
        for(x in xCoords) {
            sum += x
        }
        return sum
    }

}