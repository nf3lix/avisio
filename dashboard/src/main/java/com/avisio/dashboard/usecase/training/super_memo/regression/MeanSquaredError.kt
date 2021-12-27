package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.regression.model.Model
import com.avisio.dashboard.usecase.training.super_memo.regression.model.Point
import kotlin.math.pow

interface MeanSquaredError {

    fun calculate(d: Double): Double

    companion object {

        fun get(model: Model, points: List<Point>): Double {
            val sum: Double = points.map { p ->
                (model.getY(p.x) - p.y).pow(2.0)
            }.sum()
            return sum / points.size
        }

    }

}