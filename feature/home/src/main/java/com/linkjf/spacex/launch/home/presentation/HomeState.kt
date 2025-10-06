package com.linkjf.spacex.launch.home.presentation

import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.model.Rocket

/**
 * Represents the UI state for the Home screen
 */
data class HomeState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val selectedFilter: LaunchFilter = LaunchFilter.UPCOMING,
    val selectedTabIndex: Int = 0,
    val launches: List<Launch> = emptyList(),
    val allRockets: Map<String, Rocket> = emptyMap(),
    val allLaunchpads: Map<String, Launchpad> = emptyMap(),
    val errorMessage: String? = null,
    val rateLimitError: RateLimitError? = null,
    val hasMoreItems: Boolean = true,
    val currentPage: Int = 1,
    val pageSize: Int = 20,
    val totalCount: Int = 0,
)

/**
 * Represents the available launch filters
 */
enum class LaunchFilter {
    UPCOMING,
    PACK,
}

/**
 * Represents a rate limit error with retry information
 */
data class RateLimitError(
    val retryAfterSeconds: Int? = null,
    val message: String = "Rate limit exceeded. Please try again later.",
)
