package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.common.data.database.converters.ForgettingCurveConverter
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves
import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import org.junit.Assert
import org.junit.Test

class ForgettingCurveConverterTest {

    private val converter: ForgettingCurveConverter = ForgettingCurveConverter()

    @Test
    fun serializePointSequence() {
        val curve = ForgettingCurves.ForgettingCurve(PointSequence(Point(1.0, 1.0), Point(2.0, 2.0), Point(3.0, 3.0)))
        val serialized = converter.forgettingCurveToString(curve)
        val deserialized = converter.stringToForgettingCurve(serialized)
        Assert.assertEquals(curve, deserialized)
    }

    @Test
    fun pointSequenceEquality() {
        val sq1 = PointSequence(Point(1.0, 1.0), Point(2.0, 2.0), Point(3.0, 3.0))
        val sq2 = PointSequence(Point(1.0, 1.0), Point(2.0, 2.0), Point(3.0, 3.0))
        Assert.assertEquals(sq1, sq2)
    }

    @Test
    fun pointSequenceInequality() {
        val sq1 = PointSequence(Point(1.0, 1.0), Point(2.0, 2.0), Point(3.0, 3.0))
        val sq2 = PointSequence(Point(1.0, 1.0), Point(2.0, 2.0), Point(4.0, 3.0))
        val sq3 = PointSequence(Point(1.0, 1.0), Point(3.0, 3.0), Point(2.0, 2.0))
        Assert.assertNotEquals(sq1, sq2)
        Assert.assertNotEquals(sq1, sq3)
    }

}