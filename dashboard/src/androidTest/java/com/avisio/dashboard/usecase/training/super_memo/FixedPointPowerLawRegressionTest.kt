package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import com.avisio.dashboard.usecase.training.super_memo.regression.FixedPointPowerLawRegression
import org.junit.Assert
import org.junit.Test
import kotlin.math.pow

class FixedPointPowerLawRegressionTest {

    @Test
    fun linearFunctionAsResult() {
        val r1 = FixedPointPowerLawRegression(PointSequence(Point(1.0, 1.0), Point(2.0, 2.0), Point(3.0, 3.0)), Point(1.0, 1.0))
        val m1 = r1.compute()
        Assert.assertEquals(m1.factor, 1.0, 0.0)
        Assert.assertEquals(m1.power, 1.0, 0.0)
    }

    @Test
    fun squareFunctionAsResult() { // f(x)=x^2
        val r1 = FixedPointPowerLawRegression(PointSequence(
            Point(1.0, 1.0),
            Point(2.0, 4.0),
            Point(3.0, 9.0)
        ), Point(1.0, 1.0))
        val m1 = r1.compute()
        Assert.assertEquals(m1.factor, 1.0, 0.0)
        Assert.assertEquals(m1.power, 2.0, 1E-6)
    }

    @Test
    fun m2p3FunctionAsResult() { // f(x)=2x^3
        val r1 = FixedPointPowerLawRegression(PointSequence(
            Point(1.0, 2.0 * 1.0.pow(3)),
            Point(2.0, 2.0 * 2.0.pow(3)),
            Point(3.0, 2.0 * 3.0.pow(3))
        ), Point(1.0, 2.0 * 1.0.pow(3)))
        val m1 = r1.compute()
        Assert.assertEquals(m1.factor, 2.0, 0.0)
        Assert.assertEquals(m1.power, 3.0, 1E-6)
    }

}