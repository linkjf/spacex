package com.linkjf.spacex.launch.detail.presentation

import com.linkjf.spacex.launch.mvi.MviEvent

sealed interface DetailEvent : MviEvent {
    data object NavigateBack : DetailEvent
    data object ShowMenu : DetailEvent
    data class NavigateToWebcast(val url: String) : DetailEvent
    data class ShowError(val message: String) : DetailEvent
    data class ShowRateLimitError(val retryAfterSeconds: Int?) : DetailEvent
}

