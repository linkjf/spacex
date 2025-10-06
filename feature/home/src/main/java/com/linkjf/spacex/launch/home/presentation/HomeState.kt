package com.linkjf.spacex.launch.home.presentation

import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.model.Launchpad

/**
 * Represents the UI state for the Home screen
 */
data class HomeState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val selectedFilter: LaunchFilter = LaunchFilter.UPCOMING,
    val selectedTabIndex: Int = 0, // 0 for UPCOMING, 1 for PACK
    val launches: List<Launch> = emptyList(),
    val allRockets: Map<String, Rocket> = emptyMap(), // rocketId -> Rocket (all available rockets)
    val allLaunchpads: Map<String, Launchpad> = emptyMap(), // launchpadId -> Launchpad (all available launchpads)
    val rocketNames: Map<String, String> = emptyMap(), // rocketId -> rocketName (for individual fetches)
    val launchpadNames: Map<String, String> = emptyMap(), // launchpadId -> launchpadName (for individual fetches)
    val errorMessage: String? = null,
)

/**
 * Represents the available launch filters
 */
enum class LaunchFilter {
    UPCOMING,
    PACK,
}
