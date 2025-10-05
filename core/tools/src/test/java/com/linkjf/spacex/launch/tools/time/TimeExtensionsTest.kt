package com.linkjf.spacex.launch.tools.time

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import java.time.Instant

class TimeExtensionsTest {
    @Test
    fun `calculateTimeRemaining should return correct time for future launch`() {
        val currentTime = Instant.parse("2024-01-01T10:00:00Z")
        val launchTime =
            Instant.parse("2024-01-03T15:30:45Z") // 2 days, 5 hours, 30 minutes, 45 seconds later

        val result = calculateTimeRemaining(launchTime, currentTime)

        assertEquals(2, result.days)
        assertEquals(5, result.hours)
        assertEquals(30, result.minutes)
        assertEquals(45, result.seconds)
    }

    @Test
    fun `calculateTimeRemaining should return zero for past launch`() {
        val currentTime = Instant.parse("2024-01-03T15:30:45Z")
        val launchTime = Instant.parse("2024-01-01T10:00:00Z") // 2 days earlier

        val result = calculateTimeRemaining(launchTime, currentTime)

        assertEquals(0, result.days)
        assertEquals(0, result.hours)
        assertEquals(0, result.minutes)
        assertEquals(0, result.seconds)
    }

    @Test
    fun `calculateLaunchProgress should return correct progress for 30-day countdown`() {
        val currentTime = Instant.parse("2024-01-01T10:00:00Z")
        val launchTime = Instant.parse("2024-01-16T10:00:00Z") // 15 days later (half of 30 days)

        val result = calculateLaunchProgress(launchTime, currentTime)

        assertEquals(0.5f, result, 0.01f)
    }

    @Test
    fun `calculateLaunchProgress should return 1 for past launch and 0 for future beyond 30 days`() {
        val currentTime = Instant.parse("2024-01-16T10:00:00Z")
        val pastLaunch = Instant.parse("2024-01-01T10:00:00Z") // 15 days earlier
        val futureLaunch = Instant.parse("2024-02-15T10:00:00Z") // 45 days later

        assertEquals(1.0f, calculateLaunchProgress(pastLaunch, currentTime), 0.01f)
        assertEquals(0.0f, calculateLaunchProgress(futureLaunch, currentTime), 0.01f)
    }

    @Test
    fun `getCountdownStatus should return correct status for all scenarios`() {
        assertEquals(
            CountdownStatus.COUNTING,
            getCountdownStatus(TimeRemaining(2, 5, 30, 45), false),
        )
        assertEquals(CountdownStatus.LIVE, getCountdownStatus(TimeRemaining(2, 5, 30, 45), true))
        assertEquals(CountdownStatus.LAUNCHED, getCountdownStatus(TimeRemaining(0, 0, 0, 0), false))
        assertEquals(CountdownStatus.OVERDUE, getCountdownStatus(TimeRemaining(-1, 0, 0, 0), false))
    }

    @Test
    fun `formatToUtcString should format instant correctly`() {
        val instant = Instant.parse("2024-01-15T14:30:45Z")
        assertEquals("Jan 15, 2024 at 14:30 UTC", instant.formatToUtcString())
    }

    @Test
    fun `formatToDateString and formatToTimeString should format correctly`() {
        val instant = Instant.parse("2024-01-15T14:30:45Z")
        assertEquals("Jan 15, 2024", instant.formatToDateString())
        assertEquals("14:30", instant.formatToTimeString())
    }

    @Test
    fun `createFutureInstant and createPastInstant should create correct times`() {
        val future = createFutureInstant(days = 2, hours = 5, minutes = 30, seconds = 45)
        val past = createPastInstant(days = 2, hours = 5, minutes = 30, seconds = 45)
        val now = Instant.now()

        assertTrue("Future time should be after now", future.isAfter(now))
        assertTrue("Past time should be before now", past.isBefore(now))
    }

    @Test
    fun `TimeRemaining and CountdownStatus should work correctly`() {
        val timeRemaining = TimeRemaining(days = 1, hours = 2, minutes = 3, seconds = 4)
        assertEquals(1, timeRemaining.days)
        assertEquals(2, timeRemaining.hours)
        assertEquals(3, timeRemaining.minutes)
        assertEquals(4, timeRemaining.seconds)

        assertEquals("COUNTING", CountdownStatus.COUNTING.name)
        assertEquals("LIVE", CountdownStatus.LIVE.name)
        assertEquals("LAUNCHED", CountdownStatus.LAUNCHED.name)
        assertEquals("OVERDUE", CountdownStatus.OVERDUE.name)
    }

    @Test
    fun `integration test - complete countdown flow`() {
        val launchTime = createFutureInstant(days = 1, hours = 2, minutes = 30)
        val currentTime = Instant.now()

        val timeRemaining = calculateTimeRemaining(launchTime, currentTime)
        val progress = calculateLaunchProgress(launchTime, currentTime)
        val status = getCountdownStatus(timeRemaining, false)
        val formattedTime = launchTime.formatToUtcString()

        assertTrue("Time remaining should be valid", timeRemaining.days >= 1)
        assertTrue("Progress should be between 0 and 1", progress >= 0f && progress <= 1f)
        assertEquals("Status should be COUNTING", CountdownStatus.COUNTING, status)
        assertTrue("Formatted time should contain UTC", formattedTime.contains("UTC"))
    }

    @Test
    fun `edge cases - leap year and year boundary`() {
        val leapYearTime = Instant.parse("2024-02-29T12:00:00Z")
        val newYearTime = Instant.parse("2024-01-01T00:00:00Z")

        assertEquals("Feb 29, 2024", leapYearTime.formatToDateString())
        assertEquals("12:00", leapYearTime.formatToTimeString())
        assertEquals("Jan 01, 2024", newYearTime.formatToDateString())
        assertEquals("00:00", newYearTime.formatToTimeString())
    }
}
