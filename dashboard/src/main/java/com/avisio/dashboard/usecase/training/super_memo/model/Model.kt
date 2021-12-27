package com.avisio.dashboard.usecase.training.super_memo.model

import kotlin.math.pow

abstract class Model {

    abstract fun getX(y: Double): Double
    abstract fun getY(x: Double): Double

    fun getMeanSquaredError(points: List<Point>): Double {
        val sum: Double = points.map { p ->
            (getY(p.x) - p.y).pow(2.0)
        }.sum()
        return sum / points.size
    }

}