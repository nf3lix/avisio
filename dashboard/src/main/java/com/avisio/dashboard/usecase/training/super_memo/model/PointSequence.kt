package com.avisio.dashboard.usecase.training.super_memo.model

import java.util.*

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

    fun xCoordinates(): List<Double> {
        val x = arrayListOf<Double>()
        for(point in points) {
            x.add(point.x)
        }
        return x
    }

    fun yCoordinates(): List<Double> {
        val y = arrayListOf<Double>()
        for(point in points) {
            y.add(point.y)
        }
        return y
    }

}