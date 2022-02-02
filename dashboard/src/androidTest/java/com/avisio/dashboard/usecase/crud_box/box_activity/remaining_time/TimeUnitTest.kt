package com.avisio.dashboard.usecase.crud_box.box_activity.remaining_time

import com.avisio.dashboard.usecase.crud_box.read.due_date.TimeUnit
import org.junit.Assert
import org.junit.Test

class TimeUnitTest {

    companion object {
        private const val MINUTES: Long = 1000 * 60
        private const val HOURS: Long = MINUTES * 60
        private const val DAYS: Long = HOURS * 24
        private const val MONTHS: Long = DAYS * 30
        private const val YEARS: Long = DAYS * 364
    }

    @Test
    fun suggestMinutesFromMillisTest() {
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MINUTES * 1), TimeUnit.MINUTES)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MINUTES * 20), TimeUnit.MINUTES)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MINUTES * 45), TimeUnit.MINUTES)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MINUTES * 59), TimeUnit.MINUTES)

        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MINUTES * -1), TimeUnit.MINUTES)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MINUTES * -20), TimeUnit.MINUTES)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MINUTES * -45), TimeUnit.MINUTES)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MINUTES * -59), TimeUnit.MINUTES)
    }

    @Test
    fun suggestHoursFromMillisTest() {
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(HOURS * 1), TimeUnit.HOURS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(HOURS * 10), TimeUnit.HOURS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(HOURS * 23), TimeUnit.HOURS)

        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(HOURS * -1), TimeUnit.HOURS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(HOURS * -10), TimeUnit.HOURS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(HOURS * -23), TimeUnit.HOURS)
    }

    @Test
    fun suggestHoursFromDaysTest() {
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(DAYS * 1), TimeUnit.DAYS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(DAYS * 10), TimeUnit.DAYS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(DAYS * 29), TimeUnit.DAYS)

        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(DAYS * -1), TimeUnit.DAYS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(DAYS * -10), TimeUnit.DAYS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(DAYS * -29), TimeUnit.DAYS)
    }

    @Test
    fun suggestHoursFromMonthsTest() {
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MONTHS * 1), TimeUnit.MONTHS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MONTHS * 5), TimeUnit.MONTHS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MONTHS * 11), TimeUnit.MONTHS)

        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MONTHS * -1), TimeUnit.MONTHS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MONTHS * -5), TimeUnit.MONTHS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MONTHS * -11), TimeUnit.MONTHS)
    }

    @Test
    fun suggestedUnitFromYearsTest() {
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MONTHS * 12), TimeUnit.YEARS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(YEARS * 1), TimeUnit.YEARS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(YEARS * 3), TimeUnit.YEARS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(YEARS * 30), TimeUnit.YEARS)

        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(MONTHS * -12), TimeUnit.YEARS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(YEARS * -1), TimeUnit.YEARS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(YEARS * -3), TimeUnit.YEARS)
        Assert.assertEquals(TimeUnit.getSuggestedUnitFromMillis(YEARS * -30), TimeUnit.YEARS)
    }

    @Test
    fun millisInMinutesTest() {
        Assert.assertEquals(TimeUnit.getMillisInUnit(MINUTES * 1, TimeUnit.MINUTES), 1)
        Assert.assertEquals(TimeUnit.getMillisInUnit(MINUTES * 15, TimeUnit.MINUTES), 15)
        Assert.assertEquals(TimeUnit.getMillisInUnit(MINUTES * 60, TimeUnit.MINUTES), 60)

        Assert.assertEquals(TimeUnit.getMillisInUnit(MINUTES * -1, TimeUnit.MINUTES), -1)
        Assert.assertEquals(TimeUnit.getMillisInUnit(MINUTES * -15, TimeUnit.MINUTES), -15)
        Assert.assertEquals(TimeUnit.getMillisInUnit(MINUTES * -60, TimeUnit.MINUTES), -60)
    }

    @Test
    fun millisInHoursTest() {
        Assert.assertEquals(TimeUnit.getMillisInUnit(HOURS * 3, TimeUnit.HOURS), 3)
        Assert.assertEquals(TimeUnit.getMillisInUnit(HOURS * 5, TimeUnit.HOURS), 5)
        Assert.assertEquals(TimeUnit.getMillisInUnit(HOURS * 20, TimeUnit.HOURS), 20)

        Assert.assertEquals(TimeUnit.getMillisInUnit(HOURS * -3, TimeUnit.HOURS), -3)
        Assert.assertEquals(TimeUnit.getMillisInUnit(HOURS * -5, TimeUnit.HOURS), -5)
        Assert.assertEquals(TimeUnit.getMillisInUnit(HOURS * -20, TimeUnit.HOURS), -20)
    }

    @Test
    fun millisInDaysTest() {
        Assert.assertEquals(TimeUnit.getMillisInUnit(DAYS * 3, TimeUnit.DAYS), 3)
        Assert.assertEquals(TimeUnit.getMillisInUnit(DAYS * 5, TimeUnit.DAYS), 5)
        Assert.assertEquals(TimeUnit.getMillisInUnit(DAYS * 20, TimeUnit.DAYS), 20)

        Assert.assertEquals(TimeUnit.getMillisInUnit(DAYS * -3, TimeUnit.DAYS), -3)
        Assert.assertEquals(TimeUnit.getMillisInUnit(DAYS * -5, TimeUnit.DAYS), -5)
        Assert.assertEquals(TimeUnit.getMillisInUnit(DAYS * -20, TimeUnit.DAYS), -20)
    }

    @Test
    fun millisInMonthsTest() {
        Assert.assertEquals(TimeUnit.getMillisInUnit(MONTHS * 3, TimeUnit.MONTHS), 3)
        Assert.assertEquals(TimeUnit.getMillisInUnit(MONTHS * 5, TimeUnit.MONTHS), 5)
        Assert.assertEquals(TimeUnit.getMillisInUnit(MONTHS * 20, TimeUnit.MONTHS), 20)

        Assert.assertEquals(TimeUnit.getMillisInUnit(MONTHS * -3, TimeUnit.MONTHS), -3)
        Assert.assertEquals(TimeUnit.getMillisInUnit(MONTHS * -5, TimeUnit.MONTHS), -5)
        Assert.assertEquals(TimeUnit.getMillisInUnit(MONTHS * -20, TimeUnit.MONTHS), -20)
    }

    @Test
    fun millisInYearsTest() {
        Assert.assertEquals(TimeUnit.getMillisInUnit(YEARS * 3, TimeUnit.YEARS), 3)
        Assert.assertEquals(TimeUnit.getMillisInUnit(YEARS * 5, TimeUnit.YEARS), 5)
        Assert.assertEquals(TimeUnit.getMillisInUnit(YEARS * 20, TimeUnit.YEARS), 20)

        Assert.assertEquals(TimeUnit.getMillisInUnit(YEARS * -3, TimeUnit.YEARS), -3)
        Assert.assertEquals(TimeUnit.getMillisInUnit(YEARS * -5, TimeUnit.YEARS), -5)
        Assert.assertEquals(TimeUnit.getMillisInUnit(YEARS * -20, TimeUnit.YEARS), -20)
    }

}