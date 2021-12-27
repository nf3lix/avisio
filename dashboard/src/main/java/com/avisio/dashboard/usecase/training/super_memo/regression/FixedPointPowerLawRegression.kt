package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import com.avisio.dashboard.usecase.training.super_memo.model.PowerLawModel
import kotlin.math.ln
import kotlin.math.pow

class FixedPointPowerLawRegression(private val pointSequence: PointSequence, private val fixedPoint: Point) : Regression<PowerLawModel>() {

    override fun compute(): PowerLawModel {
        val p = fixedPoint.x
        val q = fixedPoint.y
        val logPoints = linearizedPoints()
        val power = LinearRegressionThroughOrigin(PointSequence(logPoints)).compute().m
        return PowerLawModel(power, q / p.pow(power))
    }

    private fun linearizedPoints(): List<Point> {
        val xCoords = xCoordinates()
        val yCoords = yCoordinates()
        val logPoints = arrayListOf<Point>()
        for(i in 0 until pointSequence.size()) {
            logPoints.add(Point(xCoords[i], yCoords[i]))
        }
        return logPoints
    }

    private fun xCoordinates(): List<Double> {
        val xCoords = pointSequence.xCoordinates()
        for(i in xCoords.indices) {
            xCoords[i] = ln(xCoords[i]) / fixedPoint.x
        }
        return xCoords
    }

    private fun yCoordinates(): List<Double> {
        val yCoords = pointSequence.yCoordinates()
        for(i in yCoords.indices) {
            yCoords[i] = ln(yCoords[i]) - ln(fixedPoint.y)
        }
        return yCoords
    }

}