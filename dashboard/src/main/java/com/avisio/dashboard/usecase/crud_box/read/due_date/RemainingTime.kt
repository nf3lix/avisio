package com.avisio.dashboard.usecase.crud_box.read.due_date

import java.util.*

data class RemainingTime(val dueDate: Date, private val now: Date = Date(System.currentTimeMillis())) {

    val remainingTime: Long
    val timeUnit: TimeUnit

    init {
        val remainingMillis = dueDate.time - now.time
        timeUnit = TimeUnit.getSuggestedUnitFromMillis(remainingMillis)
        remainingTime = TimeUnit.getMillisInUnit(remainingMillis, timeUnit)
    }

}