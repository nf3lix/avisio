package com.avisio.dashboard.usecase.training.super_memo.model

import kotlin.math.pow

abstract class Model {

    abstract fun getX(y: Double): Double
    abstract fun getY(x: Double): Double

    fun getMeanSquaredError(points: List<Point>): Double {
        var sum = 0.0
        for(point in points) {
            sum += (getY(point.x) - point.y).pow(2.0)
        }
        return sum / points.size
    }

}