package com.avisio.dashboard.usecase.training.super_memo.model

class LinearRegressionModel(val m: Double, val c: Double) : Model() {

    override fun getX(y: Double): Double {
        return (y - c) / m
    }

    override fun getY(x: Double): Double {
        return c + m * x
    }

}