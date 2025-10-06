package com.linkjf.spacex.launch.home.presentation

/**
 * Represents events that should trigger UI effects (navigation, showing dialogs, etc.)
 */
sealed interface HomeEvent {
    data object NavigateToSettings : HomeEvent

    data object NavigateBack : HomeEvent

    data class NavigateToLaunchDetails(
        val launchId: String,
    ) : HomeEvent

    data class OpenWebcast(
        val url: String,
    ) : HomeEvent

    data class OpenArticle(
        val url: String,
    ) : HomeEvent

    data class OpenWikipedia(
        val url: String,
    ) : HomeEvent

    data class ShowError(
        val message: String,
    ) : HomeEvent

    data object ShowRefreshing : HomeEvent

    data object HideRefreshing : HomeEvent
}
