package com.avisio.dashboard.usecase.training.super_memo.model

import com.avisio.dashboard.usecase.training.super_memo.SuperMemo
import com.avisio.dashboard.usecase.training.super_memo.SuperMemoIntf
import com.avisio.dashboard.usecase.training.super_memo.regression.ExponentialRegression
import java.util.*
import kotlin.math.max
import kotlin.math.min

class ForgettingIndexGraph(private val sm: SuperMemoIntf) {

    companion object {
        const val MAX_POINTS = 5000
        const val GRADE_OFFSET = 1
    }

    private var points = PointSequence()
    private var graph: ExponentialGraph? = null

    init {
        registerPoint(Point(0.0, SuperMemo.MAX_GRADE.toDouble()))
        registerPoint(Point(100.0, 0.0))
    }

    private fun registerPoint(point: Point) {
        points.addPoint(Point(point.x, point.y + GRADE_OFFSET))
        points = points.subSequence(max(0, points.size() - MAX_POINTS), points.size())
    }

    fun update(grade: Double, cardItem: CardItem, date: Date) {
        val expectedFI = (cardItem.uf(date) / cardItem.of()) * SuperMemo.REQUESTED_FI
        registerPoint(Point(expectedFI, grade))
        graph = null
    }

    fun forgettingIndex(grade: Double): Double {
        if(graph == null) {
            graph = ExponentialRegression(points).compute()
        }
        return max(0.0, min(100.0, graph!!.getX(grade + GRADE_OFFSET)))
    }

}