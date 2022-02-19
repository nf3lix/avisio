package com.avisio.dashboard.usecase.training.super_memo.model

import com.avisio.dashboard.common.data.model.sm.ForgettingCurveEntity
import com.avisio.dashboard.usecase.training.super_memo.MalformedForgettingCurves
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.MIN_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.NOTCH_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.RANGE_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.RANGE_REPETITION
import com.avisio.dashboard.usecase.training.super_memo.regression.ExponentialRegression
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.math.*

class ForgettingCurves {

    companion object {
        const val MAX_POINTS_COUNT = 500
        const val FORGOTTEN = 1
        const val REMEMBERED = 100 + FORGOTTEN
    }

    private val points = arrayListOf<List<Point>>()
    private var curves: ArrayList<ArrayList<ForgettingCurve>>
    private var complyInitialCurves = false

    constructor() {
        curves = initialCurves()
        complyInitialCurves = true
    }

    constructor(curvesList: List<ForgettingCurveEntity>) {
        if(curvesList.isEmpty()) {
            curves = initialCurves()
            complyInitialCurves = true
            return
        }
        curves = arrayListOf()
        parseCurveEntities(curvesList)
    }

    private fun parseCurveEntities(curvesList: List<ForgettingCurveEntity>)  {
        var count = 0
        for(i in 0 until RANGE_REPETITION) {
            val tempCurves = arrayListOf<ForgettingCurve>()
            for(j in 0 until RANGE_AF) {
                try {
                    tempCurves.add(curvesList[count].curve)
                    count++
                } catch (e: IndexOutOfBoundsException) {
                    throw MalformedForgettingCurves()
                }
            }
            curves.add(tempCurves)
        }
    }

    fun complyInitialCurves(): Boolean {
        return complyInitialCurves
    }

    private fun initialCurves(): ArrayList<ArrayList<ForgettingCurve>>  {
        val curves = arrayListOf<ArrayList<ForgettingCurve>>()
        for(r in 0 until RANGE_REPETITION) {
            val c = arrayListOf<ForgettingCurve>()
            for(a in 0 until RANGE_AF) {
                val dr = r.toDouble()
                val da = a.toDouble()
                val partialPoints = arrayListOf<Point>()
                if(points.size != 0) {
                    partialPoints.add(points[r][a])
                } else {
                    val res = arrayListOf<Point>()
                    if(r > 0) {
                        for(i in 0..20) {
                            res.add(Point(
                                MIN_AF + NOTCH_AF * i,
                                min(REMEMBERED.toDouble(), exp(-(dr + 1) / 200 * (i - da * sqrt(2 / (dr + 1)))) * (REMEMBERED - SuperMemo.REQUESTED_FI))
                            ))
                        }
                    } else {
                        for(i in 0..20) {
                            res.add(Point(
                                MIN_AF + NOTCH_AF * i,
                                min(REMEMBERED.toDouble(), exp(-1 / (10 + 1 * (da + 1)) * (i - a.toDouble().pow(0.6))) * (REMEMBERED - SuperMemo.REQUESTED_FI))
                            ))
                        }
                    }
                    partialPoints.add(Point(0.0, REMEMBERED.toDouble()))
                    partialPoints.addAll(res)
                }
                c.add(ForgettingCurve(PointSequence(partialPoints)))
            }
            curves.add(c)
        }
        return curves
    }

    fun registerPoint(grade: Double, cardItem: CardItem, date: Date) {
        val afIndex = if(cardItem.repetition() > 0) cardItem.afIndex() else cardItem.lapse()
        curves[cardItem.repetition()][afIndex].registerPoint(grade, cardItem.uf(date))
    }

    fun curves(): List<List<ForgettingCurve>> {
        return curves
    }

    class ForgettingCurve(var points: PointSequence) {

        private var graph: ExponentialGraph? = null

        fun registerPoint(grade: Double, uf: Double) {
            val isRemembered = grade >= SuperMemo.THRESHOLD_RECALL
            val yCoord = if(isRemembered) REMEMBERED else FORGOTTEN
            points.addPoint(Point(uf, yCoord.toDouble()))
            points = points.subSequence(
                max(0, points.size() - MAX_POINTS_COUNT),
                points.size())
            graph = null
        }

        fun uf(retention: Double): Double {
            if(graph == null) {
                graph = ExponentialRegression(points).compute()
            }
            return max(MIN_AF, graph!!.getX(retention + FORGOTTEN))
        }

        fun graph(): ExponentialGraph? {
            return graph
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ForgettingCurve

            if (points != other.points) return false
            if (graph != other.graph) return false

            return true
        }

        override fun hashCode(): Int {
            var result = points.hashCode()
            result = 31 * result + (graph?.hashCode() ?: 0)
            return result
        }

    }

}