package com.avisio.dashboard.usecase.crud_box.box_activity.remaining_time

import com.avisio.dashboard.usecase.crud_box.read.due_date.RemainingTime
import com.avisio.dashboard.usecase.crud_box.read.due_date.TimeUnit
import org.junit.Assert
import org.junit.Test
import java.util.*

class RemainingTimeTest {

    @Test
    fun remainingTimeMinutesTest() {
        val remainingTime1 = RemainingTime(Date(1600000000000), Date(1600000000100)) // 0min
        val remainingTime2 = RemainingTime(Date(1600000060000), Date(1600000000000)) // 1min
        val remainingTime3 = RemainingTime(Date(1600001800000), Date(1600000000000)) // 30min
        val remainingTime4 = RemainingTime(Date(1600003540000), Date(1600000000000)) // 59min
        Assert.assertEquals(remainingTime1.remainingTime, 0)
        Assert.assertEquals(remainingTime1.timeUnit, TimeUnit.MINUTES)
        Assert.assertEquals(remainingTime2.remainingTime, 1)
        Assert.assertEquals(remainingTime2.timeUnit, TimeUnit.MINUTES)
        Assert.assertEquals(remainingTime3.remainingTime, 30)
        Assert.assertEquals(remainingTime3.timeUnit, TimeUnit.MINUTES)
        Assert.assertEquals(remainingTime4.remainingTime, 59)
        Assert.assertEquals(remainingTime4.timeUnit, TimeUnit.MINUTES)

        val remainingTime5 = RemainingTime(Date(1600000000000), Date(1600000060000)) // -0min
        val remainingTime6 = RemainingTime(Date(1600000000000), Date(1600001800000)) // -1min
        val remainingTime7 = RemainingTime(Date(1600000000000), Date(1600003540000)) // -30min
        Assert.assertEquals(remainingTime5.remainingTime, -1)
        Assert.assertEquals(remainingTime5.timeUnit, TimeUnit.MINUTES)
        Assert.assertEquals(remainingTime6.remainingTime, -30)
        Assert.assertEquals(remainingTime6.timeUnit, TimeUnit.MINUTES)
        Assert.assertEquals(remainingTime7.remainingTime, -59)
        Assert.assertEquals(remainingTime7.timeUnit, TimeUnit.MINUTES)
    }

    @Test
    fun remainingTimeHoursTest() {
        val remainingTime1 = RemainingTime(Date(1600003599999), Date(1600000000000)) // 1h
        val remainingTime2 = RemainingTime(Date(1600003600000), Date(1600000000000)) // 1h
        val remainingTime3 = RemainingTime(Date(1600036000000), Date(1600000000000)) // 10h
        val remainingTime4 = RemainingTime(Date(1600082800000), Date(1600000000000)) // 23h
        Assert.assertEquals(remainingTime1.remainingTime, 1)
        Assert.assertEquals(remainingTime1.timeUnit, TimeUnit.HOURS)
        Assert.assertEquals(remainingTime2.remainingTime, 1)
        Assert.assertEquals(remainingTime2.timeUnit, TimeUnit.HOURS)
        Assert.assertEquals(remainingTime3.remainingTime, 10)
        Assert.assertEquals(remainingTime3.timeUnit, TimeUnit.HOURS)
        Assert.assertEquals(remainingTime4.remainingTime, 23)
        Assert.assertEquals(remainingTime4.timeUnit, TimeUnit.HOURS)

        val remainingTime5 = RemainingTime(Date(1600000000000), Date(1600003600000)) // -1h
        val remainingTime6 = RemainingTime(Date(1600000000000), Date(1600036000000)) // -10h
        val remainingTime7 = RemainingTime(Date(1600000000000), Date(1600082800000)) // -23h
        Assert.assertEquals(remainingTime5.remainingTime, -1)
        Assert.assertEquals(remainingTime5.timeUnit, TimeUnit.HOURS)
        Assert.assertEquals(remainingTime6.remainingTime, -10)
        Assert.assertEquals(remainingTime6.timeUnit, TimeUnit.HOURS)
        Assert.assertEquals(remainingTime7.remainingTime, -23)
        Assert.assertEquals(remainingTime7.timeUnit, TimeUnit.HOURS)
    }

    @Test
    fun remainingTimeDaysTest() {
        val remainingTime1 = RemainingTime(Date(1600086400000), Date(1600000000000)) // 1d
        val remainingTime2 = RemainingTime(Date(1600864000000), Date(1600000000000)) // 10d
        val remainingTime3 = RemainingTime(Date(1602332800000), Date(1600000000000)) // 27d
        Assert.assertEquals(remainingTime1.remainingTime, 1)
        Assert.assertEquals(remainingTime1.timeUnit, TimeUnit.DAYS)
        Assert.assertEquals(remainingTime2.remainingTime, 10)
        Assert.assertEquals(remainingTime2.timeUnit, TimeUnit.DAYS)
        Assert.assertEquals(remainingTime3.remainingTime, 27)
        Assert.assertEquals(remainingTime3.timeUnit, TimeUnit.DAYS)

        val remainingTime4 = RemainingTime(Date(1600000000000), Date(1600086400000)) // -1d
        val remainingTime5 = RemainingTime(Date(1600000000000), Date(1600864000000)) // -10d
        val remainingTime6 = RemainingTime(Date(1600000000000), Date(1602332800000)) // -27d
        Assert.assertEquals(remainingTime4.remainingTime, -1)
        Assert.assertEquals(remainingTime4.timeUnit, TimeUnit.DAYS)
        Assert.assertEquals(remainingTime5.remainingTime, -10)
        Assert.assertEquals(remainingTime5.timeUnit, TimeUnit.DAYS)
        Assert.assertEquals(remainingTime6.remainingTime, -27)
        Assert.assertEquals(remainingTime6.timeUnit, TimeUnit.DAYS)
    }

    @Test
    fun remainingTimeMonthsTest() {
        val remainingTime1 = RemainingTime(Date(1600086400000), Date(1600000000000)) // 1d
        val remainingTime2 = RemainingTime(Date(1600864000000), Date(1600000000000)) // 10d
        val remainingTime3 = RemainingTime(Date(1602332800000), Date(1600000000000)) // 27d
        Assert.assertEquals(remainingTime1.remainingTime, 1)
        Assert.assertEquals(remainingTime1.timeUnit, TimeUnit.DAYS)
        Assert.assertEquals(remainingTime2.remainingTime, 10)
        Assert.assertEquals(remainingTime2.timeUnit, TimeUnit.DAYS)
        Assert.assertEquals(remainingTime3.remainingTime, 27)
        Assert.assertEquals(remainingTime3.timeUnit, TimeUnit.DAYS)

        val remainingTime4 = RemainingTime(Date(1600000000000), Date(1600086400000)) // -1d
        val remainingTime5 = RemainingTime(Date(1600000000000), Date(1600864000000)) // -10d
        val remainingTime6 = RemainingTime(Date(1600000000000), Date(1602332800000)) // -27d
        Assert.assertEquals(remainingTime4.remainingTime, -1)
        Assert.assertEquals(remainingTime4.timeUnit, TimeUnit.DAYS)
        Assert.assertEquals(remainingTime5.remainingTime, -10)
        Assert.assertEquals(remainingTime5.timeUnit, TimeUnit.DAYS)
        Assert.assertEquals(remainingTime6.remainingTime, -27)
        Assert.assertEquals(remainingTime6.timeUnit, TimeUnit.DAYS)
    }

}