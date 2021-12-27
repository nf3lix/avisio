package com.avisio.dashboard.usecase.training.super_memo.regression.model

import android.util.Log
import com.avisio.dashboard.usecase.training.super_memo.regression.MeanSquaredError
import kotlin.math.exp
import kotlin.math.ln


/**
 * f(x)=e^(a*x) * e^c
 */

class ExponentialGraph(private val a: Double, private val c: Double) : Model() {

    override fun getX(y: Double): Double {
        return (-c + ln(y)) / a
    }

    override fun getY(x: Double): Double {
        return exp(a * x) * exp(c)
    }

    /*
    fun getMSE(x: Double, points: ArrayList<Point>): Double {
        return MeanSquaredError.get(object : MeanSquaredError {
            override fun calculate(d: Double): Double {
                return getY(x)
            }
        }, points)
    }*/

}