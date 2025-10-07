package com.linkjf.spacex.launch.home.presentation

data class HomeState(
    val selectedFilter: LaunchFilter = LaunchFilter.UPCOMING,
    val selectedTabIndex: Int = TAB_INDEX_UPCOMING,
    val errorMessage: String? = null,
    val rateLimitError: RateLimitError? = null,
) {
    companion object {
        const val TAB_INDEX_UPCOMING = 0
        const val TAB_INDEX_PAST = 1
    }
}

enum class LaunchFilter {
    UPCOMING,
    PACK,
}

data class RateLimitError(
    val retryAfterSeconds: Int? = null,
    val message: String = "Rate limit exceeded. Please try again later.",
)
