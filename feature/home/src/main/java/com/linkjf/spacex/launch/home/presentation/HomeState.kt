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
)

/**
 * Represents the available launch filters
 */
enum class LaunchFilter {
    UPCOMING,
    PACK,
}
