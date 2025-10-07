package com.linkjf.spacex.launch.detail.presentation

import com.linkjf.spacex.launch.mvi.MviState

data class DetailState(
    val isLoading: Boolean = true,
    val launchId: String = "",
    val launchName: String = "",
    val launchCode: String = "",
    val dateUtc: String = "",
    val patchImageUrl: String? = null,
    val status: LaunchDetailStatus = LaunchDetailStatus.SCHEDULED,
    val isLive: Boolean = false,
    val rocketName: String = "",
    val launchpadName: String = "",
    val location: String = "",
    val details: String? = null,
    val webcastUrl: String? = null,
    val weatherData: WeatherDetailData = WeatherDetailData(),
    val errorMessage: String? = null,
    val rateLimitError: RateLimitError? = null,
) : MviState

enum class LaunchDetailStatus {
    SCHEDULED,
    LIVE,
    LAUNCHED,
    FAILED,
    CANCELLED,
}

data class WeatherDetailData(
    val windSpeed: String = "--",
    val windGust: String = "--",
    val cloudCover: String = "--",
    val precipitationProbability: String = "--",
    val visibility: String = "--",
)

data class RateLimitError(
    val retryAfterSeconds: Int? = null,
    val message: String = "Rate limit exceeded. Please try again later.",
)

