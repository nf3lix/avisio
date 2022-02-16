package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.LinearModel
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence

class LinearRegression(private val pointSequence: PointSequence) : Regression<LinearModel>() {

    override fun compute(): LinearModel {
        val n = pointSequence.size()
        val sumX = pointSequence.sumX()
        val sumY = pointSequence.sumY()
        val sumSqX = sumSquaredX()
        val sumXY = pointSequence.sumXY()
        val sqSumX = sumX * sumX
        val c = (sumY * sumSqX - sumX * sumXY) / (n * sumSqX - sqSumX)
        val m = (n * sumXY - sumX * sumY) / (n * sumSqX - sqSumX)
        return LinearModel(m, c)
    }

    private fun sumSquaredX(): Double {
        val sqX = pointSequence.squaredXCoordinates()
        var sumSqX = 0.0
        for(x in sqX) {
            sumSqX += x
        }
        return sumSqX
    }

}