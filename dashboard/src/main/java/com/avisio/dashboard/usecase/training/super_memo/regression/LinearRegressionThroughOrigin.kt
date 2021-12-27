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

    private fun sumSquaredX(xCoords: ArrayList<Double>): Double {
        val squared = squaredXCoordinates(xCoords)
        return xCoordinatesSum(squared)
    }

    private fun sumXY(xCoords: List<Double>, yCoords: List<Double>): Double {
        var sumXY = 0.0
        for(i in 0 until points.size()) {
            sumXY += xCoords[i] * yCoords[i]
        }
        return sumXY
    }

    private fun squaredXCoordinates(xCoords: ArrayList<Double>): ArrayList<Double> {
        for(i in xCoords.indices) {
            xCoords[i] = xCoords[i].pow(2)
        }
        return xCoords
    }

    private fun xCoordinatesSum(xCoords: ArrayList<Double>): Double {
        var sum = 0.0
        for(x in xCoords) {
            sum += x
        }
        return sum
    }

}