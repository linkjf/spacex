package com.linkjf.spacex.launch.home.presentation

import com.linkjf.spacex.launch.designsystem.components.LaunchListItem

sealed interface HomeAction {
    data object TapSettings : HomeAction

    data class SelectFilter(
        val filter: LaunchFilter,
    ) : HomeAction

    data class SelectTab(
        val tabIndex: Int,
    ) : HomeAction

    data class TapLaunch(
        val launchItem: LaunchListItem,
    ) : HomeAction

    data class TapWatch(
        val launchId: String,
    ) : HomeAction

    data object DismissError : HomeAction

    data object DismissRateLimitError : HomeAction
}
