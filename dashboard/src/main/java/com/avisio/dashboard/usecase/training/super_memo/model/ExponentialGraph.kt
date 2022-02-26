package com.avisio.dashboard.usecase.training.super_memo.model

import kotlin.math.exp
import kotlin.math.ln


/**
 * f(x)=e^(a*x) * e^c
 */

class ExponentialGraph(val a: Double, val b: Double) : Model() {

    override fun getX(y: Double): Double {
        return (-b + ln(y)) / a
    }

    override fun getY(x: Double): Double {
        return exp(a * x) * exp(b)
    }

}