package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.ExponentialGraph
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import kotlin.math.ln
import kotlin.math.pow

class ExponentialRegression(private val points: PointSequence) : Regression<ExponentialGraph>() {

    override fun compute(): ExponentialGraph {
        val n = points.size()
        val sumLogY = sumLogY()
        val sumSqX = sumSquaredX()
        val sumX = points.sumX()
        val sqSumX = sumX.pow(2)
        val sumXLogY = sumXLogY()
        val a = (n * sumXLogY - sumX * sumLogY) / (n * sumSqX - sqSumX)
        val b = (sumLogY * sumSqX - sumX * sumXLogY) / (n * sumSqX - sqSumX)
        return ExponentialGraph(a, b)
    }

    private fun logY(): ArrayList<Double> {
        val yCoords = points.yCoordinates()
        for(i in yCoords.indices) {
            yCoords[i] = ln(yCoords[i])
        }
        return yCoords
    }

    private fun sumLogY(): Double {
        val logY = logY()
        var sumLogY = 0.0
        for(y in logY) {
            sumLogY += y
        }
        return sumLogY
    }

    private fun sumSquaredX(): Double {
        val sqX = points.squaredXCoordinates()
        var sumSqX = 0.0
        for(x in sqX) {
            sumSqX += x
        }
        return sumSqX
    }

    private fun sumXLogY(): Double {
        var sumXLogY = 0.0
        val logY = logY()
        val xCoords = points.xCoordinates()
        for(i in 0 until points.size()) {
            sumXLogY += xCoords[i] * logY[i]
        }
        return sumXLogY
    }

}