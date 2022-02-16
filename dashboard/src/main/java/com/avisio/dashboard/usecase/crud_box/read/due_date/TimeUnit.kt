package com.avisio.dashboard.usecase.crud_box.read.due_date

import com.avisio.dashboard.R
import kotlin.math.abs
import kotlin.math.roundToLong

enum class TimeUnit(private val highestDisplayedValue: Double, private val inSeconds: Int, val prefixString: Int) {

    MINUTES(
        59.0,
        60,
        R.plurals.minutes),
    HOURS(
        23.0,
        MINUTES.inSeconds * 60,
        R.plurals.hours),
    DAYS(
        29.0,
        HOURS.inSeconds * 24,
        R.plurals.days),
    MONTHS(
        11.5,
        DAYS.inSeconds * 30,
        R.plurals.months),
    YEARS(
        Double.MAX_VALUE,
        DAYS.inSeconds * 364,
        R.plurals.years);

    companion object {

        fun getSuggestedUnitFromMillis(remainingTimeInMillis: Long): TimeUnit {
            val timeInSeconds = abs(remainingTimeInMillis) / 1000
            for(unit in values()) {
                if(timeInSeconds <= unit.highestDisplayedValue * unit.inSeconds) {
                    return unit
                }
            }
            return YEARS
        }

        fun getMillisInUnit(millis: Long, unit: TimeUnit): Long {
            return (millis.toDouble() / 1000.0 / unit.inSeconds.toDouble()).roundToLong()
        }

    }

}