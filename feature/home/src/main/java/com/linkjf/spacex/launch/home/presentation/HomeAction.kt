package com.linkjf.spacex.launch.home.presentation

/**
 * Represents all possible user actions in the Home screen
 */
sealed interface HomeAction {
    // Navigation actions
    data object TapSettings : HomeAction

    // Filter actions
    data class SelectFilter(
        val filter: LaunchFilter,
    ) : HomeAction

    data class SelectTab(
        val tabIndex: Int,
    ) : HomeAction

    // Refresh and loading actions
    data object PullToRefresh : HomeAction

    data object LoadMore : HomeAction

    // Launch item actions
    data class TapLaunch(
        val launchId: String,
    ) : HomeAction

    data class TapWatch(
        val launchId: String,
    ) : HomeAction

    // Error handling
    data object DismissError : HomeAction

    data object Retry : HomeAction
}
