package com.linkjf.spacex.launch.home.presentation

data class HomeState(
    val selectedFilter: LaunchFilter = LaunchFilter.UPCOMING,
    val selectedTabIndex: Int = 0,
    val errorMessage: String? = null,
    val rateLimitError: RateLimitError? = null,
)

enum class LaunchFilter {
    UPCOMING,
    PACK,
}

data class RateLimitError(
    val retryAfterSeconds: Int? = null,
    val message: String = "Rate limit exceeded. Please try again later.",
)
