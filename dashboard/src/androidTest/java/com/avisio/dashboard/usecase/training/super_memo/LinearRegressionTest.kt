package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import com.avisio.dashboard.usecase.training.super_memo.regression.LinearRegression
import org.junit.Assert
import org.junit.Test

class LinearRegressionTest {

    @Test
    fun linearFunctionAsResult() {
        val r = LinearRegression(PointSequence(Point(0.0, 0.0), Point(1.0, 1.0), Point(2.0, 2.0))).compute() // f(X)=x
        val r2 = LinearRegression(PointSequence(Point(0.0, 1.0), Point(1.0, 3.0), Point(2.0, 5.0))).compute() // f(x)=2x+1
        Assert.assertEquals(r.m, 1.0, 0.0)
        Assert.assertEquals(r.c, 0.0, 0.0)
        Assert.assertEquals(r2.m, 2.0, 0.0)
        Assert.assertEquals(r2.c, 1.0, 0.0)
    }

}