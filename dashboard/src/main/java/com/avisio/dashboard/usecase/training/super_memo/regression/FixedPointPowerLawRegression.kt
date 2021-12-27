package com.avisio.dashboard.usecase.training.super_memo.regression

import android.util.Log
import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import com.avisio.dashboard.usecase.training.super_memo.model.PowerLawModel
import kotlin.math.ln
import kotlin.math.pow

class FixedPointPowerLawRegression(private val pointSequence: PointSequence, private val fixedPoint: Point) : Regression<PowerLawModel>() {

    override fun compute(): PowerLawModel {
        val p = fixedPoint.x
        val q = fixedPoint.y
        val logQ = ln(q)
        val xCoords = pointSequence.xCoordinates().map { x -> ln(x) / p}
        val yCoords = pointSequence.yCoordinates().map { y -> ln(y) - logQ}
        val logPoints = arrayListOf<Point>()
        Log.d("regression", xCoords.toString())
        Log.d("regression", yCoords.toString())
        for(i in 0 until pointSequence.size()) {
            logPoints.add(Point(xCoords[i], yCoords[i]))
        }
        val power = LinearRegressionThroughOrigin(PointSequence(logPoints)).compute().m
        Log.d("regression", logQ.toString())
        Log.d("regression", logPoints.toString())
        return PowerLawModel(power, q / p.pow(power))
    }

}