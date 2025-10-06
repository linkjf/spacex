package com.linkjf.spacex.launch.home.presentation

/**
 * Represents all possible user actions in the Home screen
 */
sealed interface HomeAction {
    data object TapSettings : HomeAction

    data class SelectFilter(
        val filter: LaunchFilter,
    ) : HomeAction

    data class SelectTab(
        val tabIndex: Int,
    ) : HomeAction

    data object PullToRefresh : HomeAction

    data object LoadMore : HomeAction

    data class TapLaunch(
        val launchId: String,
    ) : HomeAction

    data class TapWatch(
        val launchId: String,
    ) : HomeAction

    data object DismissError : HomeAction

    data object DismissRateLimitError : HomeAction

    data object Retry : HomeAction
}
