package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import com.avisio.dashboard.usecase.training.super_memo.regression.LinearRegressionThroughOrigin
import org.junit.Assert
import org.junit.Test

class LinearRegressionThroughOriginTest {

    @Test
    fun identityFunctionAsResult() {
        val r1 = LinearRegressionThroughOrigin(PointSequence(Point(1.0, 1.0), Point(2.0, 2.0), Point(3.0, 3.0)))
        val m1 = r1.compute().m
        Assert.assertEquals(m1, 1.0, 0.0)

        val r2 = LinearRegressionThroughOrigin(PointSequence(Point(1.0, 0.0), Point(2.0, 1.0), Point(3.0, 4.0)))
        val m2 = r2.compute().m
        Assert.assertEquals(m2, 1.0, 0.0)
    }

    @Test
    fun m2FunctionAsResult() {
        val r1 = LinearRegressionThroughOrigin(PointSequence(Point(1.0, 2.0), Point(2.0, 4.0), Point(3.0, 6.0)))
        val m1 = r1.compute().m
        Assert.assertEquals(m1, 2.0, 0.0)

        val r2 = LinearRegressionThroughOrigin(PointSequence(Point(1.0, 2.0), Point(2.0, 7.0), Point(3.0, 4.0)))
        val m2 = r2.compute().m
        Assert.assertEquals(m2, 2.0, 0.0)
    }

    @Test
    fun m0FunctionAsResult() {
        val r1 = LinearRegressionThroughOrigin(PointSequence(Point(1.0, 0.0), Point(2.0, 0.0), Point(3.0, 0.0)))
        val m1 = r1.compute().m
        Assert.assertEquals(m1, 0.0, 0.0)
    }

}