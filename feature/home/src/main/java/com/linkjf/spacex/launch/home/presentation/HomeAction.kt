package com.linkjf.spacex.launch.home.presentation

sealed interface HomeAction {
    data object TapSettings : HomeAction

    data class SelectFilter(
        val filter: LaunchFilter,
    ) : HomeAction

    data class SelectTab(
        val tabIndex: Int,
    ) : HomeAction

    data class TapLaunch(
        val launchId: String,
    ) : HomeAction

    data class TapWatch(
        val launchId: String,
    ) : HomeAction

    data object DismissError : HomeAction

    data object DismissRateLimitError : HomeAction
}
