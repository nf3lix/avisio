package com.avisio.dashboard.usecase.training.super_memo.regression.model

class LRThroughOriginModel(private val a: Double): Model() {

    override fun getX(y: Double): Double {
        return y / a
    }

    override fun getY(x: Double): Double {
        return a * x
    }

}