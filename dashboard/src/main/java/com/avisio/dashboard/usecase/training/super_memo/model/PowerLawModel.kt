package com.avisio.dashboard.usecase.training.super_memo.model

import kotlin.math.pow

class PowerLawModel(val power: Double, val factor: Double) : Model() {

    override fun getX(y: Double): Double {
        return (y / factor).pow(1 / power)
    }

    override fun getY(x: Double): Double {
        return factor * x.pow(power)
    }

}