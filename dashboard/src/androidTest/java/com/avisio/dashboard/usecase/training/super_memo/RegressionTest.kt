package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.regression.model.*
import org.junit.Assert
import org.junit.Test
import java.lang.Math.*
import kotlin.math.pow

class RegressionTest {

    @Test
    fun powerLawModelTest() {
        val p1 = PowerLawModel(2.0, 3.0) // f(x)=3x^2
        val p2 = PowerLawModel(3.0, 2.0) // f(x)=2x^3
        Assert.assertEquals(p1.getY(1.0), 3.0, 0.0)
        Assert.assertEquals(p1.getY(2.0), 12.0, 0.0)
        Assert.assertEquals(p1.getY(3.0), 27.0, 0.0)
        Assert.assertEquals(p1.getY(4.0), 48.0, 0.0)
        Assert.assertEquals(p2.getX(2.0), 1.0, 0.0)
        Assert.assertEquals(p2.getX(16.0), 2.0, 0.0)
        Assert.assertEquals(p2.getX(54.0), 3.0, 0.0)
        Assert.assertEquals(p2.getX(128.0), 4.0, 1E-4)
    }

    @Test
    fun lrThroughOriginResultTest() {
        val result1 = LRThroughOriginModel(3.0) // f(x)=3x
        val result2 = LRThroughOriginModel(4.0) // f(x)=4x
        Assert.assertEquals(result1.getY(1.0), 3.0, 0.0)
        Assert.assertEquals(result1.getY(2.0), 6.0, 0.0)
        Assert.assertEquals(result1.getX(3.0), 1.0, 0.0)
        Assert.assertEquals(result1.getX(6.0), 2.0, 0.0)
        Assert.assertEquals(result2.getY(1.0), 4.0, 0.0)
        Assert.assertEquals(result2.getY(2.0), 8.0, 0.0)
        Assert.assertEquals(result2.getX(4.0), 1.0, 0.0)
        Assert.assertEquals(result2.getX(8.0), 2.0, 0.0)
    }

    @Test
    fun linearRegressionTest() {
        val l1 = LinearRegressionModel(3.0, 2.0) // f(x)=3x^2
        Assert.assertEquals(l1.getY(1.0), (3 * 1 + 2).toDouble(), 0.0)
        Assert.assertEquals(l1.getY(2.0), (3 * 2 + 2).toDouble(), 0.0)
        Assert.assertEquals(l1.getY(3.0), (3 * 3 + 2).toDouble(), 0.0)
        Assert.assertEquals(l1.getX((3 * 1 + 2).toDouble()), 1.0, 0.0)
        Assert.assertEquals(l1.getX((3 * 2 + 2).toDouble()), 2.0, 0.0)
        Assert.assertEquals(l1.getX((3 * 3 + 2).toDouble()), 3.0, 0.0)
    }

    @Test
    fun exponentialGraphTest() {
        val g1 = ExponentialGraph(1.0, 0.0) // f(x)=e^x
        val g2 = ExponentialGraph(3.0, 2.0) // f(x)=e^(3x)*e^2

        Assert.assertEquals(g1.getY(0.0), 1.0, 1E-4)
        Assert.assertEquals(g1.getY(1.0), E, 1E-4)
        Assert.assertEquals(g1.getY(2.0), E.pow(2), 1E-4)
        Assert.assertEquals(g1.getX(1.0), 0.0, 1E-4)
        Assert.assertEquals(g1.getX(E), 1.0, 1E-4)
        Assert.assertEquals(g1.getX(E.pow(2)), 2.0, 1E-4)

        Assert.assertEquals(g2.getY(1.0), E.pow(1 * 3) * E.pow(2), 1E-4)
        Assert.assertEquals(g2.getY(2.0), E.pow(2 * 3) * E.pow(2), 1E-4)
        Assert.assertEquals(g2.getY(3.0), E.pow(3 * 3) * E.pow(2), 1E-4)
        Assert.assertEquals(g2.getX(E.pow(1 * 3) * E.pow(2)), 1.0, 1E-4)
        Assert.assertEquals(g2.getX(E.pow(2 * 3) * E.pow(2)), 2.0, 1E-4)
        Assert.assertEquals(g2.getX(E.pow(3 * 3) * E.pow(2)), 3.0, 1E-4)
    }

    @Test
    fun exponentialGraphMSE0Test() {
        val g1 = ExponentialGraph(1.0, 0.0) // f(x)=e^x
        val points1 = arrayListOf(
            Point(1.0, exp(1.0)),
            Point(2.0, exp(2.0)),
            Point(3.0, exp(3.0))
        )
        Assert.assertEquals(g1.getMeanSquaredError(points1), 0.0, 0.0)

        val g2 = ExponentialGraph(4.0, 3.0) // f(x)=e^(4x)*e^3
        val points2 = arrayListOf(
            Point(1.0, exp(1.0 * 4.0) * exp(3.0)),
            Point(2.0, exp(2.0 * 4.0) * exp(3.0)),
            Point(3.0, exp(3.0 * 4.0) * exp(3.0))
        )
        Assert.assertEquals(g2.getMeanSquaredError(points2), 0.0, 0.0)
    }

    @Test
    fun exponentialGraphMSENotZeroTest() {
        val g1 = ExponentialGraph(1.0, 0.0) // f(x)=e^x
        val points1 = arrayListOf(
            Point(1.0, exp(1.0) + 1),
            Point(2.0, exp(2.0) + 1),
            Point(3.0, exp(3.0) + 1)
        )
        Assert.assertEquals(g1.getMeanSquaredError(points1), 1.0, 0.0)

        val g2 = ExponentialGraph(4.0, 3.0) // f(x)=e^(4x)*e^3
        val points2 = arrayListOf(
            Point(1.0, exp(1.0 * 4.0) * exp(3.0)),
            Point(2.0, exp(2.0 * 4.0) * exp(3.0) + 1),
            Point(3.0, exp(3.0 * 4.0) * exp(3.0))
        )
        Assert.assertEquals(g2.getMeanSquaredError(points2), (1.0 / 3.0).toDouble(), 1E-6)
    }

    companion object {
        fun exp(d: Double): Double {
            return kotlin.math.exp(d)
        }
    }

}