package com.linkjf.spacex.launch.core.time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeConstants {
    const val LAUNCH_DATE_INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val LAUNCH_DATE_INPUT_FORMAT_WITH_MS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val LAUNCH_DATE_OUTPUT_FORMAT = "MMM/dd/yyyy"
    const val LAUNCH_TIME_OUTPUT_FORMAT = "HH:mm"
    const val UNKNOWN_DATE = "Unknown"
    const val UNKNOWN_TIME = "Unknown"
    const val COMPLETED_STATUS = "Completed"
    const val LIVE_STATUS = "Live"
    const val TBD_STATUS = "TBD"
    const val LAUNCHED_STATUS = "Launched"
}

object TimeUtils {


    fun String.formatToLaunchDate(): String =
        try {
            val inputFormat =
                SimpleDateFormat(TimeConstants.LAUNCH_DATE_INPUT_FORMAT, Locale.getDefault())
            val outputFormat =
                SimpleDateFormat(TimeConstants.LAUNCH_DATE_OUTPUT_FORMAT, Locale.getDefault())
            val date = inputFormat.parse(this)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            try {
                val inputFormatMs =
                    SimpleDateFormat(
                        TimeConstants.LAUNCH_DATE_INPUT_FORMAT_WITH_MS,
                        Locale.getDefault(),
                    )
                val outputFormat =
                    SimpleDateFormat(TimeConstants.LAUNCH_DATE_OUTPUT_FORMAT, Locale.getDefault())
                val date = inputFormatMs.parse(this)
                outputFormat.format(date ?: Date())
            } catch (e2: Exception) {
                this.split("T")[0]
            }
        }

    /**
     * Formats a UTC date string to a readable time format
     */
    fun String.formatToLaunchTime(): String =
        try {
            val inputFormat =
                SimpleDateFormat(TimeConstants.LAUNCH_DATE_INPUT_FORMAT, Locale.getDefault())
            val outputFormat =
                SimpleDateFormat(TimeConstants.LAUNCH_TIME_OUTPUT_FORMAT, Locale.getDefault())
            val date = inputFormat.parse(this)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            try {
                val inputFormatMs =
                    SimpleDateFormat(
                        TimeConstants.LAUNCH_DATE_INPUT_FORMAT_WITH_MS,
                        Locale.getDefault(),
                    )
                val outputFormat =
                    SimpleDateFormat(TimeConstants.LAUNCH_TIME_OUTPUT_FORMAT, Locale.getDefault())
                val date = inputFormatMs.parse(this)
                outputFormat.format(date ?: Date())
            } catch (e2: Exception) {
                try {
                    this.split("T")[1]
                        .split(":")[0] + ":" +
                            this.split("T")[1]
                                .split(":")[1]
                } catch (e3: Exception) {
                    "00:00"
                }
            }
        }

    fun String.calculateCountdown(isUpcoming: Boolean): String {
        if (!isUpcoming) return TimeConstants.COMPLETED_STATUS

        return try {
            val inputFormat =
                SimpleDateFormat(TimeConstants.LAUNCH_DATE_INPUT_FORMAT, Locale.getDefault())
            val launchDate = inputFormat.parse(this) ?: return TimeConstants.TBD_STATUS
            val now = Date()
            val diffInMillis = launchDate.time - now.time

            if (diffInMillis <= 0) return TimeConstants.LAUNCHED_STATUS

            val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
            val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60

            when {
                days > 0 -> "${days}d ${hours}h"
                hours > 0 -> "${hours}h ${minutes}min"
                else -> "${minutes}min"
            }
        } catch (e: Exception) {
            try {
                val inputFormatMs =
                    SimpleDateFormat(
                        TimeConstants.LAUNCH_DATE_INPUT_FORMAT_WITH_MS,
                        Locale.getDefault(),
                    )
                val launchDate = inputFormatMs.parse(this) ?: return TimeConstants.TBD_STATUS
                val now = Date()
                val diffInMillis = launchDate.time - now.time

                if (diffInMillis <= 0) return TimeConstants.LAUNCHED_STATUS

                val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60

                when {
                    days > 0 -> "${days}d ${hours}h"
                    hours > 0 -> "${hours}h ${minutes}min"
                    else -> "${minutes}min"
                }
            } catch (e2: Exception) {
                TimeConstants.TBD_STATUS
            }
        }
    }

    fun formatDuration(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60

        return when {
            hours > 0 -> "${hours}h ${minutes}m ${secs}s"
            minutes > 0 -> "${minutes}m ${secs}s"
            else -> "${secs}s"
        }
    }
}
