package com.linkjf.spacex.launch.home.presentation

sealed interface HomeEvent {
    data object NavigateToSettings : HomeEvent

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
}
