package com.avisio.dashboard.usecase.training.super_memo.model

import java.util.*
import kotlin.math.pow

class PointSequence {

    constructor(vararg point: Point) {
        points.addAll(point)
    }

    constructor(pointsList: List<Point>) {
        points.addAll(pointsList)
    }

    private val points: LinkedList<Point> = LinkedList()

    fun size(): Int {
        return points.size
    }

    fun addPoint(point: Point) {
        points.add(findIndexToInsert(point), point)
    }

    fun subSequence(start: Int, end: Int): PointSequence {
        return PointSequence(points.subList(start, end))
    }

    fun xCoordinates(): ArrayList<Double> {
        val x = arrayListOf<Double>()
        for(point in points) {
            x.add(point.x)
        }
        return x
    }

    fun yCoordinates(): ArrayList<Double> {
        val y = arrayListOf<Double>()
        for(point in points) {
            y.add(point.y)
        }
        return y
    }

    fun sumX(): Double {
        var sum = 0.0
        for(x in xCoordinates()) {
            sum += x
        }
        return sum
    }

    fun sumY(): Double {
        var sum = 0.0
        for(y in yCoordinates()) {
            sum += y
        }
        return sum
    }

    fun sumXY(): Double {
        var sumXY = 0.0
        for(i in 0 until points.size) {
            sumXY += points[i].x * points[i].y
        }
        return sumXY
    }

    fun squaredXCoordinates(): ArrayList<Double> {
        val xCoords = xCoordinates()
        for(i in xCoords.indices) {
            xCoords[i] = xCoords[i].pow(2)
        }
        return xCoords
    }

    private fun findIndexToInsert(point: Point): Int {
        if(points.size == 0) {
            return 0
        }
        if(points.size == 1) {
            if(points[0].x <= point.x) {
                return 1
            }
            return 0
        }
        for(i in 1 until points.size) {
            val prevPoint = points[i - 1]
            val currentPoint = points[i]
            if(prevPoint.x < point.x && currentPoint.x > point.x) {
                return i
            }
        }
        return points.size
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PointSequence
        if(points.size != other.size()) return false
        for((index, p) in points.withIndex()) {
            if(p != other.points[index]) {
                return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        return points.hashCode()
    }


}