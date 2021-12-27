package com.avisio.dashboard.usecase.training.super_memo.model

import java.util.*
import kotlin.collections.ArrayList
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

}