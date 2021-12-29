package com.avisio.dashboard.usecase.training.super_memo

import com.avisio.dashboard.usecase.training.super_memo.model.*
import com.avisio.dashboard.usecase.training.super_memo.ofm.OFM
import com.avisio.dashboard.usecase.training.super_memo.rfm.RFM
import java.util.*

class SuperMemo : SuperMemoIntf {

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

    private val queue = CardQueue()
    private val forgettingIndexGraph = ForgettingIndexGraph(this)
    private val forgettingCurves = ForgettingCurves(this)
    private val rfm: RFM = RFM(this)
    private val ofm = OFM(this)

    fun update(grade: Double, cardItem: CardItem, date: Date): Double {
        if(cardItem.repetition() >= 0) {
            forgettingCurves.registerPoint(grade, cardItem, date)
            ofm.update()
            forgettingIndexGraph.update(grade, cardItem, date)
        }
        return cardItem.answer(grade, date)
    }

    fun answer(grade: Double, cardItem: CardItem) {
        update(grade, cardItem, Date(System.currentTimeMillis()))
        queue.answer(cardItem)
    }

    override fun queue(): CardQueue {
        return queue
    }

    override fun rfm(): RFM {
        return rfm
    }

    override fun ofm(): OFM {
        return ofm
    }

    override fun forgettingIndexGraph(): ForgettingIndexGraph {
        return forgettingIndexGraph
    }

    override fun forgettingCurves(): ForgettingCurves {
        return forgettingCurves
    }



}