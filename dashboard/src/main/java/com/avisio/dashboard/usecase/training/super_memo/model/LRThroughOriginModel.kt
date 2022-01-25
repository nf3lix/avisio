package com.avisio.dashboard.usecase.training.super_memo.model

class LRThroughOriginModel(val m: Double): Model() {

    override fun getX(y: Double): Double {
        return y / m
    }

    override fun getY(x: Double): Double {
        return m * x
    }

}