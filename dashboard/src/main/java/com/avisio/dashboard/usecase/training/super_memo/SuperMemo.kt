package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.CardQueue
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingCurves
import com.avisio.dashboard.usecase.training.super_memo.model.ForgettingIndexGraph

class SuperMemo {

    companion object {
        const val RANGE_AF = 20
        const val RANGE_REPETITION = 20
        const val MIN_AF = 1.2
        const val NOTCH_AF = 0.3
        const val MAX_AF = MIN_AF * NOTCH_AF * (RANGE_AF - 1)
        const val MAX_GRADE = 5
        const val THRESHOLD_RECALL = 3
        const val REQUESTED_FI = 10
        const val INTERVAL_BASE = 3 * 60 * 60 * 1000
    }

    private val queue = CardQueue(this)
    private val fi_g = ForgettingIndexGraph(this)
    private val forgettingCurves = ForgettingCurves(this)

}