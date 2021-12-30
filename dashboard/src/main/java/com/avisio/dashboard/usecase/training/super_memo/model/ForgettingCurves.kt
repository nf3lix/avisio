package com.avisio.dashboard.usecase.training.super_memo.model

import android.util.Log
import com.avisio.dashboard.usecase.training.super_memo.SuperMemoIntf
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.MIN_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.NOTCH_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.RANGE_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.RANGE_REPETITION
import com.avisio.dashboard.usecase.training.super_memo.regression.ExponentialRegression
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

class ForgettingCurves(private val sm: SuperMemoIntf) {

    companion object {
        const val MAX_POINTS_COUNT = 500
        const val FORGOTTEN = 1
        const val REMEMBERED = 100 + FORGOTTEN
    }

    private val points = arrayListOf<List<Point>>()
    private var curves: List<List<ForgettingCurve>>

    init {
        curves = initialCurves()
    }

    private fun initialCurves(): List<List<ForgettingCurve>>  {
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
                                min(REMEMBERED.toDouble(), exp((-(dr + 1) / 200) * (i - da * sqrt(2 / (dr + 1)))) * (REMEMBERED - SuperMemo.REQUESTED_FI))
                            ))
                        }
                    } else {
                        for(i in 0..20) {
                            res.add(Point(
                                MIN_AF + NOTCH_AF * i,
                                min(REMEMBERED.toDouble(), exp((-1 / (10 + 1 * (da + 1))) * (i - a.toDouble().pow(0.6))) * (REMEMBERED - SuperMemo.REQUESTED_FI))
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

    class ForgettingCurve(private var points: PointSequence) {

        private var graph: ExponentialGraph? = null

        fun registerPoint(grade: Double, uf: Double) {
            val isRemembered = grade >= SuperMemo.THRESHOLD_RECALL
            val yCoord = if(isRemembered) REMEMBERED else FORGOTTEN
            points.addPoint(Point(uf, yCoord.toDouble()))
            points = points.subSequence(
                max(0, (points.size() - MAX_POINTS_COUNT)),
                points.size())
            graph = null
        }

        fun uf(retention: Double): Double {
            if(graph == null) {
                graph = ExponentialRegression(points).compute()
            }
            return max(0.0, graph!!.getX(retention + FORGOTTEN))
        }

        override fun toString(): String {
            return points.toString()
        }

        fun graph(): ExponentialGraph? {
            return graph
        }

    }

}