package com.avisio.dashboard.usecase.training.super_memo.retention

import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves
import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import org.junit.Assert
import org.junit.Test

class ForgettingCurveTest {

    @Test
    fun registerPoint() {
        val curve = standardForgettingCurve()
        updateGraph(curve)
        Assert.assertEquals(curve.graph()!!.a, -0.2961760279708177, 1E-3)
        Assert.assertEquals(curve.graph()!!.c, 4.842871655888891, 1E-3)
        curve.registerPoint(5.0, 1.2)
        updateGraph(curve)
        Assert.assertEquals(curve.graph()!!.a, -0.2994732512960212, 1E-3)
        Assert.assertEquals(curve.graph()!!.c, 4.860656644621813, 1E-3)
        curve.registerPoint(5.0, 1.3)
        curve.registerPoint(5.0, 2.0)
        curve.registerPoint(5.0, 3.0)
        curve.registerPoint(5.0, 4.0)
        curve.registerPoint(5.0, 5.0)
        updateGraph(curve)
        Assert.assertEquals(curve.graph()!!.a, -0.2935573940825819, 1E-3)
        Assert.assertEquals(curve.graph()!!.c, 4.954689792331937, 1E-3)
        //Assert.assertEquals(curve., 4.842871655888891, 1E-3)
    }

    private fun updateGraph(curve: ForgettingCurves.ForgettingCurve) {
        curve.uf(0.0)
    }

    private fun standardForgettingCurve(): ForgettingCurves.ForgettingCurve {
        return ForgettingCurves.ForgettingCurve(
            PointSequence(
                Point(x=0.0, y=101.0), Point(x=1.2, y=91.0), Point(x=1.336842962962963, y=101.0),
                Point(x=1.5, y=83.09216518168587), Point(x=1.7999999999999998, y=75.87151554484143),
                Point(x=2.0999999999999996, y=69.27833518941551), Point(x=2.4, y=63.25809748429796),
                Point(x=2.7, y=57.76101412356565), Point(x=3.0, y=52.74162336941767), Point(x=3.3, y=48.15841407650458),
                Point(x=3.5999999999999996, y=43.973482388274114), Point(x=3.8999999999999995, y=40.15221826615854),
                Point(x=4.2, y=36.6630192591511), Point(x=4.5, y=33.477029146601254), Point(x=4.8, y=30.56789929276378),
                Point(x=5.1, y=27.911570739466665), Point(x=5.4, y=25.48607523477005), Point(x=5.7, y=23.27135355209215),
                Point(x=6.0, y=21.249089597273116), Point(x=6.3, y=19.402558931616056), Point(x=6.6, y=17.716490458167424),
                Point(x=6.9, y=16.17694012736054), Point(x=7.2, y=14.771175617548181)))
    }

}