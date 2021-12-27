package com.avisio.dashboard.usecase.training.super_memo.regression.model

import kotlin.math.pow

class PowerLawModel(private val power: Double, private val factor: Double) : Model() {

    override fun getX(y: Double): Double {
        return (y / factor).pow(1 / power)
    }

    override fun getY(x: Double): Double {
        return factor * x.pow(power)
    }

}