package com.avisio.dashboard.usecase.training.super_memo.ofm

import com.avisio.dashboard.usecase.training.super_memo.SuperMemoIntf
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.MIN_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.NOTCH_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.RANGE_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.RANGE_REPETITION
import com.avisio.dashboard.usecase.training.super_memo.model.*
import com.avisio.dashboard.usecase.training.super_memo.ofm.OFMModel.*
import com.avisio.dashboard.usecase.training.super_memo.regression.ExponentialRegression
import com.avisio.dashboard.usecase.training.super_memo.regression.FixedPointPowerLawRegression
import com.avisio.dashboard.usecase.training.super_memo.regression.LinearRegression
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.pow

class OFM(private val sm: SuperMemoIntf) {

    companion object {
        const val INITIAL_REPETITION_VALUE = 1

        fun afFromIndex(index: Int) = index * NOTCH_AF + MIN_AF
        fun repFromIndex(r: Double) = r + INITIAL_REPETITION_VALUE

    }

    private lateinit var ofm0: OFM0
    private lateinit var ofm: OFMN

    init {
        update()
    }

    fun update() {
        updateOFMN()
        updateOFM0()
    }

    fun of(repetition: Int, afIndex: Int): Double {
        if(repetition == 0) {
            return ofm0.get(afIndex)
        }
        return ofm.get(afIndex).getY(repetition.toDouble())
    }

    fun af(repetition: Int, of: Double): Double {
        val iaf = arrayListOf<Int>()
        for(i in 0 until RANGE_AF) {
            iaf.add(i)
        }
        val index = reduceAFIndices(iaf, repetition, of)
        return afFromIndex(index)
    }

    private fun reduceAFIndices(iaf: ArrayList<Int>, repetition: Int, of: Double): Int {
        while(iaf.size > 1) {
            val a = iaf[0]
            val b = iaf[1]
            if(abs(of(repetition, a) - of) < abs(of(repetition, b) - of)) {
                iaf[1] = a
            } else {
                iaf[1] = b
            }
            iaf.removeAt(0)
        }
        return iaf[0]
    }

    private fun updateOFMN() {
        val points = decayPoints()
        val decay = LinearRegression(PointSequence(points)).compute()
        ofm = object : OFMN {
            override fun get(i: Int): PointModel {
                val af = afFromIndex(i)
                val b = ln(af / decay.getY(i.toDouble())) / ln(repFromIndex(1.0))
                val model = PowerLawModel(b, af / repFromIndex(1.0).pow(b))
                return PointModel(model)
            }
        }
    }

    private fun updateOFM0() {
        val firstRow = arrayListOf<Point>()
        for(i in 0 until RANGE_AF) {
            firstRow.add(Point(i.toDouble(), sm.rfm().rf(0, i)))
        }
        val g = ExponentialRegression(PointSequence(firstRow)).compute()
        ofm0 = object : OFM0 {
            override fun get(i: Int): Double {
                return g.getY(i.toDouble())
            }
        }
    }

    private fun decayPoints(): ArrayList<Point> {
        val dfs = dFactors2()
        for(i in 0 until RANGE_AF) {
            dfs[i] = afFromIndex(i) / 2.0.pow(dfs[i])
        }
        val points = arrayListOf<Point>()
        for(i in 0 until RANGE_AF) {
            points.add(Point(i.toDouble(), dfs[i]))
        }
        return points
    }

    private fun dFactors2(): ArrayList<Double> {
        val dfs = arrayListOf<Double>()
        for(i in 0 until RANGE_AF) {
            val points = arrayListOf<Point>()
            for(j in 1 until RANGE_REPETITION) {
                points.add(Point(repFromIndex(j.toDouble()), sm.rfm().rf(j, i)))
            }
            val fixedPoint = Point(repFromIndex(1.0), afFromIndex(i))
            val rb = FixedPointPowerLawRegression(PointSequence(points), fixedPoint).compute().power
            dfs.add(rb)
        }
        return dfs
    }

    private fun computeRFactorPowerApproximation(): List<PowerLawModel> {
        val approximations = arrayListOf<PowerLawModel>()
        for(i in 0 until RANGE_AF) {
            val points = arrayListOf<Point>()
            for(j in 1 until RANGE_REPETITION) {
                points.add(Point(repFromIndex(j.toDouble()), sm.rfm().rf(j, i)))
            }
            val fixedPoint = Point(2.0, afFromIndex(i))
            val approximation = FixedPointPowerLawRegression(PointSequence(points), fixedPoint).compute()
            approximations.add(approximation)
        }
        return approximations
    }

    private fun dFactors(powerApproximations: List<PowerLawModel>): List<Double> {
        val decayConstants = arrayListOf<Double>()
        for(approximation in powerApproximations) {
            decayConstants.add(approximation.power)
        }
        return decayConstants
    }

    private fun approximateDFactorChange(dFactors: List<Double>): LinearModel {
        val points = arrayListOf<Point>()
        for((x, y) in dFactors.withIndex()) {
            points.add(Point(x.toDouble(), y))
        }
        return LinearRegression(PointSequence(points)).compute()
    }

    private fun firstRowOFM(): ExponentialGraph {
        val firstRow = arrayListOf<Point>()
        for(i in 0 until RANGE_AF) {
            firstRow.add(Point(i.toDouble(), sm.rfm().rf(0, i)))
        }
        return ExponentialRegression(PointSequence(firstRow)).compute()
    }

}