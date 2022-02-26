package com.avisio.dashboard.usecase.training.super_memo.regression

import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import org.junit.Assert
import org.junit.Test
import kotlin.math.pow

class ExponentialRegressionTest {

    @Test
    fun constFunctionAsResult() { // f(x)=1
        val r = ExponentialRegression(PointSequence(
            Point(0.0, 1.0), Point(1.0, 1.0), Point(2.0, 1.0)
        )).compute()
        Assert.assertEquals(r.a, 1.0, 0.0)
        Assert.assertEquals(r.b, 0.0, 0.0)
    }

    @Test
    fun exponentialFunction() { // f(x)=e^x
        val r = ExponentialRegression(PointSequence(
            Point(0.0, 1.0), Point(1.0, Math.E), Point(2.0, Math.E.pow(2))
        )).compute()
        Assert.assertEquals(r.a, 1.0, 0.0)
        Assert.assertEquals(r.b, 1.0, 0.0)
    }

    @Test
    fun exponentialFunction2() { // f(x)=e^(2x)*e
        val r = ExponentialRegression(PointSequence(
            Point(0.0, Math.E), Point(1.0, Math.E.pow(2) * Math.E), Point(2.0, Math.E.pow(4) * Math.E)
        )).compute()
        Assert.assertEquals(r.a, 2.0, 0.0)
        Assert.assertEquals(r.b, 1.0, 0.0)
    }

    @Test
    fun exponentialFunction3() { // f(x)=e^(3x)*e^2
        val r = ExponentialRegression(PointSequence(
            Point(0.0, Math.E.pow(2)),
            Point(1.0, Math.E.pow(1 * 3) * Math.E.pow(2)),
            Point(2.0, Math.E.pow(2 * 3) * Math.E.pow(2))
        )).compute()
        Assert.assertEquals(r.a, 3.0, 0.0)
        Assert.assertEquals(r.b, 2.0, 0.0)
    }

}