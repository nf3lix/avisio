package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import com.avisio.dashboard.usecase.training.super_memo.model.PowerLawModel
import kotlin.math.exp
import kotlin.math.ln

class FixedPointPowerLawRegression(private val pointSequence: PointSequence, private val fixedPoint: Point) : Regression<PowerLawModel>() {

    override fun compute(): PowerLawModel {
        val logPoints = logTransformedPoints()
        val linearModel = LinearRegression(logPoints).compute()
        return PowerLawModel(
            power = linearModel.m,
            factor = exp(linearModel.c)
        )
    }

    private fun logTransformedPoints(): PointSequence {
        val logPoints = arrayListOf<Point>()
        val xCoordinates = pointSequence.xCoordinates()
        val yCoordinates = pointSequence.yCoordinates()
        for(pointIndex in 0 until pointSequence.size()) {
            logPoints.add(Point(
                ln(xCoordinates[pointIndex]),
                ln(yCoordinates[pointIndex])
            ))
        }
        return PointSequence(logPoints)
    }
}