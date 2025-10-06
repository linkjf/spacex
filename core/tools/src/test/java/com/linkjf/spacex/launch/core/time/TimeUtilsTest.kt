package com.linkjf.spacex.launch.core.time

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeUtilsTest {
    @Test
    fun `formatToLaunchDate should format valid date string correctly`() {
        val inputDate = "2024-01-15T14:30:45Z"
        val result = TimeUtils.run { inputDate.formatToLaunchDate() }
        assertEquals("Jan/15/2024", result)
    }

    @Test
    fun `formatToLaunchDate should handle date with milliseconds`() {
        val inputDate = "2024-01-15T14:30:45.123Z"
        val result = TimeUtils.run { inputDate.formatToLaunchDate() }
        assertEquals("Jan/15/2024", result)
    }

    @Test
    fun `formatToLaunchDate should fallback to date part for invalid format`() {
        val inputDate = "2024-01-15T14:30:45"
        val result = TimeUtils.run { inputDate.formatToLaunchDate() }
        assertEquals("2024-01-15", result)
    }

    @Test
    fun `formatToLaunchTime should format valid time string correctly`() {
        val inputDate = "2024-01-15T14:30:45Z"
        val result = TimeUtils.run { inputDate.formatToLaunchTime() }
        assertEquals("14:30", result)
    }

    @Test
    fun `formatToLaunchTime should handle time with milliseconds`() {
        val inputDate = "2024-01-15T14:30:45.123Z"
        val result = TimeUtils.run { inputDate.formatToLaunchTime() }
        assertEquals("14:30", result)
    }

    @Test
    fun `formatToLaunchTime should fallback to HHmm for invalid format`() {
        val inputDate = "2024-01-15T14:30:45"
        val result = TimeUtils.run { inputDate.formatToLaunchTime() }
        assertEquals("14:30", result)
    }

    @Test
    fun `formatToLaunchTime should return 0000 for completely invalid format`() {
        val inputDate = "invalid-date"
        val result = TimeUtils.run { inputDate.formatToLaunchTime() }
        assertEquals("00:00", result)
    }

    @Test
    fun `calculateCountdown should return COMPLETED for past launches`() {
        val pastDate = "2020-01-15T14:30:45Z"
        val result = TimeUtils.run { pastDate.calculateCountdown(isUpcoming = false) }
        assertEquals(TimeConstants.COMPLETED_STATUS, result)
    }

    @Test
    fun `calculateCountdown should return TBD for invalid date`() {
        val invalidDate = "invalid-date"
        val result = TimeUtils.run { invalidDate.calculateCountdown(isUpcoming = true) }
        assertEquals(TimeConstants.TBD_STATUS, result)
    }

    @Test
    fun `calculateCountdown should return LAUNCHED for past upcoming launch`() {
        val pastDate = "2020-01-15T14:30:45Z"
        val result = TimeUtils.run { pastDate.calculateCountdown(isUpcoming = true) }
        assertEquals(TimeConstants.LAUNCHED_STATUS, result)
    }

    @Test
    fun `calculateCountdown should return TBD for invalid upcoming launch`() {
        val invalidDate = "invalid-date"
        val result = TimeUtils.run { invalidDate.calculateCountdown(isUpcoming = true) }
        assertEquals(TimeConstants.TBD_STATUS, result)
    }

    @Test
    fun `TimeConstants should have correct values`() {
        assertEquals("yyyy-MM-dd'T'HH:mm:ss'Z'", TimeConstants.LAUNCH_DATE_INPUT_FORMAT)
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", TimeConstants.LAUNCH_DATE_INPUT_FORMAT_WITH_MS)
        assertEquals("MMM/dd/yyyy", TimeConstants.LAUNCH_DATE_OUTPUT_FORMAT)
        assertEquals("HH:mm", TimeConstants.LAUNCH_TIME_OUTPUT_FORMAT)
        assertEquals("Unknown", TimeConstants.UNKNOWN_DATE)
        assertEquals("Unknown", TimeConstants.UNKNOWN_TIME)
        assertEquals("Completed", TimeConstants.COMPLETED_STATUS)
        assertEquals("Live", TimeConstants.LIVE_STATUS)
        assertEquals("TBD", TimeConstants.TBD_STATUS)
        assertEquals("Launched", TimeConstants.LAUNCHED_STATUS)
    }
}
