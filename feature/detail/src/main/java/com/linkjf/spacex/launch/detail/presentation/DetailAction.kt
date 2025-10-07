package com.linkjf.spacex.launch.detail.presentation


sealed interface DetailAction {
    data object OnBackClicked : DetailAction
    data object OnMenuClicked : DetailAction
    data object OnWatchClicked : DetailAction
    data object OnRefresh : DetailAction
    data object DismissError : DetailAction
    data object DismissRateLimitError : DetailAction
}

