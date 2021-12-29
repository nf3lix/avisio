package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.Point
import com.avisio.dashboard.usecase.training.super_memo.model.PointSequence
import org.junit.Assert.assertEquals
import org.junit.Test

class PointSequenceTest {

    @Test
    fun constructorTest() {
        val sequence1 = PointSequence(Point(0.0, 0.0), Point(0.0, 0.0))
        val sequence2 = PointSequence(Point(0.0, 0.0), Point(0.0, 0.0), Point(0.0, 0.0))
        val sequence3 = PointSequence(arrayListOf(Point(0.0, 0.0), Point(0.0, 0.0)))
        val sequence4 = PointSequence(arrayListOf(Point(0.0, 0.0), Point(0.0, 0.0), Point(0.0, 0.0)))
        assertEquals(sequence1.size(), 2)
        assertEquals(sequence2.size(), 3)
        assertEquals(sequence3.size(), 2)
        assertEquals(sequence4.size(), 3)
    }

    @Test
    fun addPoint() {
        val sequence = PointSequence()
        assertEquals(sequence.size(), 0)
        sequence.addPoint(Point(0.0, 0.0))
        assertEquals(sequence.size(), 1)
        sequence.addPoint(Point(0.0, 0.0))
        assertEquals(sequence.size(), 2)
    }

    @Test
    fun xCoordinates() {
        val sequence = PointSequence(Point(0.0, 0.0), Point(1.0, 0.0), Point(2.0, 0.0))
        val xCoords = sequence.xCoordinates()
        assertEquals(xCoords, arrayListOf(0.0, 1.0, 2.0))
    }

    @Test
    fun yCoordinates() {
        val sequence = PointSequence(Point(0.0, 0.0), Point(0.0, 1.0), Point(0.0, 2.0))
        val yCoords = sequence.yCoordinates()
        assertEquals(yCoords, arrayListOf(0.0, 1.0, 2.0))
    }

    @Test
    fun sumX() {
        val sequence = PointSequence(Point(1.0, 0.0), Point(2.0, 0.0), Point(3.0, 0.0))
        assertEquals(sequence.sumX(), 6.0, 0.0)
    }

    @Test
    fun sumY() {
        val sequence = PointSequence(Point(0.0, 1.0), Point(0.0, 2.0), Point(0.0, 3.0))
        assertEquals(sequence.sumY(), 6.0, 0.0)
    }

    @Test
    fun sumXY() {
        val sequence = PointSequence(Point(2.0, 1.0), Point(3.0, 2.0), Point(4.0, 3.0))
        assertEquals(sequence.sumXY(), 2.0 * 1.0 + 3.0 * 2.0 + 4.0 * 3.0, 0.0)
    }

    @Test
    fun squaredXCoordinates() {
        val sequence = PointSequence(Point(2.0, 0.0), Point(3.0, 0.0), Point(4.0, 0.0))
        assertEquals(sequence.squaredXCoordinates(), arrayListOf(4.0, 9.0, 16.0))
    }

    @Test
    fun subSequence() {
        val sequence = PointSequence(Point(2.0, 1.0), Point(3.0, 2.0), Point(4.0, 3.0))
        val s1 = sequence.subSequence(0, 2)
        val s2 = sequence.subSequence(1, 3)
        assertEquals(s1.xCoordinates(), arrayListOf(2.0, 3.0))
        assertEquals(s1.yCoordinates(), arrayListOf(1.0, 2.0))
        assertEquals(s2.xCoordinates(), arrayListOf(3.0, 4.0))
        assertEquals(s2.yCoordinates(), arrayListOf(2.0, 3.0))
    }

}