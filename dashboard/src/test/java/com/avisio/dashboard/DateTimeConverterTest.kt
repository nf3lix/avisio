package com.avisio.dashboard

import com.avisio.dashboard.common.data.database.DateTimeConverter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DateTimeConverterTest {

    private val converter: DateTimeConverter = DateTimeConverter()

    companion object {
        private const val SAMPLE_TIMESTAMP_2019 = 1546300800000
        private const val SAMPLE_TIMESTAMP_2020 = 1577836800000
        private const val SAMPLE_TIMESTAMP_2021 = 1609459200000
    }

    @Test
    fun toDateTest() {
        val date2019 = converter.toDate(SAMPLE_TIMESTAMP_2019)
        val date2020 = converter.toDate(SAMPLE_TIMESTAMP_2020)
        val date2021 = converter.toDate(SAMPLE_TIMESTAMP_2021)
        assertEquals(date2019, Date(SAMPLE_TIMESTAMP_2019))
        assertEquals(date2020, Date(SAMPLE_TIMESTAMP_2020))
        assertEquals(date2021, Date(SAMPLE_TIMESTAMP_2021))
    }

    @Test
    fun toTimestampTest() {
        val date2019 = converter.fromDateToLong(Date(SAMPLE_TIMESTAMP_2019))
        val date2020 = converter.fromDateToLong(Date(SAMPLE_TIMESTAMP_2020))
        val date2021 = converter.fromDateToLong(Date(SAMPLE_TIMESTAMP_2021))
        assertEquals(SAMPLE_TIMESTAMP_2019, date2019)
        assertEquals(SAMPLE_TIMESTAMP_2020, date2020)
        assertEquals(SAMPLE_TIMESTAMP_2021, date2021)
    }
}