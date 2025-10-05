package com.linkjf.spacex.launch.tools.time

import java.time.Duration
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Calculates the time remaining between current time and launch time.
 * Returns a TimeRemaining object with days, hours, minutes, and seconds.
 */
fun calculateTimeRemaining(
    launchTime: Instant,
    currentTime: Instant,
): TimeRemaining {
    val duration = Duration.between(currentTime, launchTime)

    return if (duration.isNegative) {
        TimeRemaining(0, 0, 0, 0)
    } else {
        TimeRemaining(
            days = duration.toDays().toInt(),
            hours = (duration.toHours() % 24).toInt(),
            minutes = (duration.toMinutes() % 60).toInt(),
            seconds = (duration.seconds % 60).toInt(),
        )
    }
}

/**
 * Calculates the progress percentage for a launch countdown.
 * Assumes a 30-day countdown period and returns a value between 0.0 and 1.0.
 */
fun calculateLaunchProgress(
    launchTime: Instant,
    currentTime: Instant,
): Float {
    val totalDuration = Duration.ofDays(30) // Assume 30 days before launch
    val remainingDuration = Duration.between(currentTime, launchTime)

    return if (remainingDuration.isNegative) {
        1f
    } else {
        (totalDuration.minus(remainingDuration).toMillis().toFloat() / totalDuration.toMillis().toFloat())
    }.coerceIn(0f, 1f)
}

/**
 * Determines the countdown status based on time remaining and live status.
 */
fun getCountdownStatus(
    timeRemaining: TimeRemaining,
    isLive: Boolean,
): CountdownStatus =
    when {
        isLive -> CountdownStatus.LIVE
        timeRemaining.days == 0 && timeRemaining.hours == 0 &&
            timeRemaining.minutes == 0 && timeRemaining.seconds == 0 -> CountdownStatus.LAUNCHED
        timeRemaining.days < 0 || timeRemaining.hours < 0 ||
            timeRemaining.minutes < 0 || timeRemaining.seconds < 0 -> CountdownStatus.OVERDUE
        else -> CountdownStatus.COUNTING
    }

/**
 * Formats an Instant to a human-readable UTC string.
 * Format: "MMM dd, yyyy 'at' HH:mm 'UTC'"
 */
fun Instant.formatToUtcString(): String =
    this
        .atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm 'UTC'"))

/**
 * Formats an Instant to a compact date string.
 * Format: "MMM dd, yyyy"
 */
fun Instant.formatToDateString(): String =
    this
        .atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))

/**
 * Formats an Instant to a time string.
 * Format: "HH:mm"
 */
fun Instant.formatToTimeString(): String =
    this
        .atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("HH:mm"))

/**
 * Creates an Instant from a future time relative to now.
 * Useful for creating sample countdown data.
 */
fun createFutureInstant(
    days: Long = 0,
    hours: Long = 0,
    minutes: Long = 0,
    seconds: Long = 0,
): Instant =
    Instant
        .now()
        .plus(Duration.ofDays(days))
        .plus(Duration.ofHours(hours))
        .plus(Duration.ofMinutes(minutes))
        .plus(Duration.ofSeconds(seconds))

/**
 * Creates an Instant from a past time relative to now.
 * Useful for creating sample historical data.
 */
fun createPastInstant(
    days: Long = 0,
    hours: Long = 0,
    minutes: Long = 0,
    seconds: Long = 0,
): Instant =
    Instant
        .now()
        .minus(Duration.ofDays(days))
        .minus(Duration.ofHours(hours))
        .minus(Duration.ofMinutes(minutes))
        .minus(Duration.ofSeconds(seconds))

/**
 * Data class representing time remaining in a countdown.
 */
data class TimeRemaining(
    val days: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
)

/**
 * Enum representing different countdown states.
 */
enum class CountdownStatus {
    COUNTING,
    LIVE,
    LAUNCHED,
    OVERDUE,
}
