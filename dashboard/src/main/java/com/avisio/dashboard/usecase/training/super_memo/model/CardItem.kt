package com.avisio.dashboard.usecase.training.super_memo.model

import com.avisio.dashboard.common.data.model.sm.SMCardItem
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.INTERVAL_BASE
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.MAX_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.MIN_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.NOTCH_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.RANGE_AF
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.RANGE_REPETITION
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.REQUESTED_FI
import com.avisio.dashboard.usecase.training.super_memo.SuperMemo.Companion.THRESHOLD_RECALL
import com.avisio.dashboard.usecase.training.super_memo.SuperMemoIntf
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.*

class CardItem(
    private val sm: SuperMemoIntf,
    val cardId: Long = -1,
    var dueDate: Date = Date(System.currentTimeMillis()),
    private var lapse: Int = 0,
    private var repetition: Int = -1,
    private var of: Double = 1.0,
    private var af: Double = -1.0,
    private var afs: ArrayList<Double> = arrayListOf(),
    private var optimumInterval: Double = INTERVAL_BASE.toDouble(),
    private var previousDate: Date? = null
) {

    companion object {
        const val MAX_AFS_COUNT = 30

        fun fromSMCardItem(sm: SuperMemoIntf, cardItem: SMCardItem): CardItem {
            val dueDate =
                if (cardItem.dueDate == Date(0)) Date(System.currentTimeMillis()) else cardItem.dueDate
            val previousDate = if (cardItem.previousDate == Date(0)) null else cardItem.previousDate
            return CardItem(
                sm = sm,
                cardId = cardItem.cardId,
                dueDate = dueDate,
                lapse = cardItem.lapse,
                repetition = cardItem.repetition,
                of = cardItem.of,
                af = cardItem.af,
                afs = cardItem.afs,
                optimumInterval = cardItem.optimumInterval,
                previousDate = previousDate
            )
        }
    }

    private fun interval(date: Date): Long {
        if(previousDate == null) {
            return INTERVAL_BASE.toLong()
        }
        return date.time - previousDate!!.time
    }

    fun dueDate(): Date {
        return dueDate
    }

    fun previousDate(): Date? {
        return previousDate
    }

    fun uf(date: Date): Double {
        return interval(date) / (optimumInterval / of)
    }

    private fun af(value: Double): Double {
        val a = ((value - MIN_AF) / NOTCH_AF).roundToLong().toInt()
        af = MIN_AF.coerceAtLeast(MAX_AF.coerceAtMost(MIN_AF + a * NOTCH_AF))
        return af
    }

    fun af(): Double {
        if(af == -1.0) {
            return MIN_AF
        }
        return af
    }

    fun afs(): ArrayList<Double> {
        return afs
    }

    fun optimumInterval(): Double {
        return optimumInterval
    }

    fun afIndex(): Int {
        val list = arrayListOf<Double>()
        val lc = arrayListOf<Int>()
        for (i in 0 until RANGE_AF) {
            list.add(MIN_AF + i * NOTCH_AF)
            lc.add(i)
        }
        while (lc.size > 1) {
            val x1 = lc[0]
            val x2 = lc[1]
            if (abs(af() - list[x1]) < abs(af() - list[x2])) {
                lc[1] = x1
            } else {
                lc[1] = x2
            }
            lc.removeAt(0)
        }
        return lc[0]
    }

    private fun i(date: Date): Long {
        val index = if(repetition == 0) lapse else afIndex()
        val newOf = sm.ofm().of(repetition, index)
        of = max(1.0, (newOf - 1) * (interval(date) / optimumInterval) + 1)
        optimumInterval *= of
        previousDate = date
        dueDate = Date(date.time + optimumInterval.toLong())
        return dueDate.time
    }

    private fun updateAF(grade: Double, date: Date) {
        val estimatedFI = max(1.0, sm.forgettingIndexGraph().forgettingIndex(grade))
        val correctedUF = uf(date) * (REQUESTED_FI / estimatedFI)
        val estimatedAF =
            if(repetition > 0) sm.ofm().af(repetition, correctedUF)
            else max(MIN_AF, min(MAX_AF, correctedUF))
        afs.add(estimatedAF)
        val nAfs = afs.subList(max(0, afs.size - MAX_AFS_COUNT), afs.size)
        val nAfsArrayList = arrayListOf<Double>()
        for(i in nAfs) {
            nAfsArrayList.add(i)
        }
        afs = nAfsArrayList
        val afsCopy = afs
        var x = 0.0
        for(i in afsCopy.indices) {
            x += afsCopy[i] * (i + 1)
        }
        var y = 0.0
        for(i in afsCopy.indices) {
            y += i
        }
        val t = af(x / y)
    }

    fun answer(grade: Double, date: Date): Double {
        if(repetition >= 0) {
            updateAF(grade, date)
        }
        if(grade >= THRESHOLD_RECALL) {
            if(repetition < RANGE_REPETITION - 1) {
                repetition++
            }
            return i(date).toDouble()
        }
        if(lapse < RANGE_AF - 1) {
            lapse++
        }
        optimumInterval = INTERVAL_BASE.toDouble()
        previousDate = null
        dueDate = date
        repetition = -1
        return repetition.toDouble()
    }

    fun of(): Double {
        return of
    }

    fun repetition(): Int {
        return repetition
    }

    fun lapse(): Int {
        return lapse
    }

}