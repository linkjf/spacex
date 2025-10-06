package com.linkjf.spacex.launch.home.presentation

import androidx.lifecycle.viewModelScope
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.model.PaginatedLaunches
import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import com.linkjf.spacex.launch.mvi.StateViewModel
import com.linkjf.spacex.launch.network.exceptions.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getUpcomingLaunches: GetUpcomingLaunchesUseCase,
        private val getPastLaunches: GetPastLaunchesUseCase,
    ) : StateViewModel<HomeState, HomeEvent, HomeAction>(HomeState()) {
        init {
            loadInitialData()
        }

        override fun reduce(action: HomeAction) {
            when (action) {
                HomeAction.TapSettings -> HomeEvent.NavigateToSettings.sendToEvent()

                is HomeAction.SelectFilter -> selectFilter(action.filter)
                is HomeAction.SelectTab -> selectTab(action.tabIndex)

                HomeAction.PullToRefresh -> refresh()
                HomeAction.LoadMore -> loadMore()

                is HomeAction.TapLaunch ->
                    HomeEvent
                        .NavigateToLaunchDetails(action.launchId)
                        .sendToEvent()

                is HomeAction.TapWatch -> handleWatchAction(action.launchId)

                HomeAction.DismissError -> dismissError()
                HomeAction.DismissRateLimitError -> dismissRateLimitError()
                HomeAction.Retry -> retry()
            }
        }

        private fun loadInitialData() {
            if (state.value.launches.isNotEmpty()) return

            state.value.copy(isLoading = true).sendToState()

            // Load rocket and launchpad data first, then load launches
            viewModelScope.launch {
                // Load all data for rocket/launchpad mapping
                getUpcomingLaunches(20, 0)
                    .onEach { result ->
                        result.fold(
                            onSuccess = { paginatedLaunches ->
                                // Process upcoming launches for mapping
                                processLaunchesForMapping(paginatedLaunches.launches, isUpcoming = true)
                            },
                            onFailure = { error ->
                                handleError(error)
                            },
                        )
                    }.launchIn(viewModelScope)

                getPastLaunches(20, 0)
                    .onEach { result ->
                        result.fold(
                            onSuccess = { paginatedLaunches ->
                                // Process past launches for mapping
                                processLaunchesForMapping(paginatedLaunches.launches, isUpcoming = false)

                                // After processing both, load the first page of launches
                                loadLaunches()
                            },
                            onFailure = { error ->
                                handleError(error)
                            },
                        )
                    }.launchIn(viewModelScope)
            }
        }

        private fun selectTab(tabIndex: Int) {
            val filter = if (tabIndex == 0) LaunchFilter.UPCOMING else LaunchFilter.PACK
            selectFilter(filter)
        }

        private fun selectFilter(filter: LaunchFilter) {
            if (state.value.selectedFilter == filter) return

            state.value
                .copy(
                    selectedFilter = filter,
                    selectedTabIndex = if (filter == LaunchFilter.UPCOMING) 0 else 1,
                    launches = emptyList(),
                    isLoading = true,
                    currentPage = 1,
                    hasMoreItems = true,
                ).sendToState()

            loadLaunches()
        }

        private fun refresh() {
            if (state.value.isRefreshing) return

            state.value.copy(isRefreshing = true).sendToState()
            HomeEvent.ShowRefreshing.sendToEvent()

            viewModelScope.launch {
                loadLaunches()
                state.value.copy(isRefreshing = false).sendToState()
                HomeEvent.HideRefreshing.sendToEvent()
            }
        }

        private fun loadMore() {
            if (state.value.isLoadingMore || !state.value.hasMoreItems || state.value.launches.isEmpty()) return

            state.value.copy(isLoadingMore = true).sendToState()

            val nextPage = state.value.currentPage + 1
            val offset = (nextPage - 1) * state.value.pageSize

            val flow =
                when (state.value.selectedFilter) {
                    LaunchFilter.UPCOMING -> getUpcomingLaunches(state.value.pageSize, offset)
                    LaunchFilter.PACK -> getPastLaunches(state.value.pageSize, offset)
                }

            flow
                .onEach { result ->
                    result.fold(
                        onSuccess = { paginatedLaunches ->
                            val currentLaunches = state.value.launches
                            val updatedLaunches = currentLaunches + paginatedLaunches.launches

                            state.value
                                .copy(
                                    launches = updatedLaunches,
                                    isLoadingMore = false,
                                    currentPage = nextPage,
                                    hasMoreItems = paginatedLaunches.hasMore,
                                    totalCount = paginatedLaunches.totalCount,
                                    errorMessage = null,
                                    rateLimitError = null,
                                ).sendToState()
                        },
                        onFailure = { error ->
                            state.value
                                .copy(
                                    isLoadingMore = false,
                                    errorMessage = getErrorMessage(error),
                                    rateLimitError = null,
                                ).sendToState()
                            handleError(error)
                        },
                    )
                }.launchIn(viewModelScope)
        }

        private fun handleWatchAction(launchId: String) {
            val launch = state.value.launches.find { it.id == launchId }
            when {
                launch?.links?.webcast != null -> {
                    HomeEvent.OpenWebcast(launch.links.webcast).sendToEvent()
                }

                launch?.links?.youtubeId != null -> {
                    HomeEvent
                        .OpenWebcast("https://youtube.com/watch?v=${launch.links.youtubeId}")
                        .sendToEvent()
                }

                else -> {
                    HomeEvent.ShowError("No webcast available for this launch").sendToEvent()
                }
            }
        }

        private fun dismissError() {
            state.value.copy(errorMessage = null).sendToState()
        }

        private fun dismissRateLimitError() {
            state.value.copy(rateLimitError = null).sendToState()
        }

        private fun retry() {
            dismissError()
            loadInitialData()
        }

        private fun processLaunchesForMapping(
            launches: List<com.linkjf.spacex.launch.home.domain.model.Launch>,
            isUpcoming: Boolean,
        ) {
            val allRockets = state.value.allRockets.toMutableMap()
            val allLaunchpads = state.value.allLaunchpads.toMutableMap()

            launches.forEach { launch ->
                launch.rocket?.let { rocket ->
                    allRockets[rocket.id] = rocket
                }
                launch.launchpad?.let { launchpad ->
                    allLaunchpads[launchpad.id] = launchpad
                }
            }

            state.value
                .copy(
                    allRockets = allRockets,
                    allLaunchpads = allLaunchpads,
                ).sendToState()
        }

        private fun loadLaunches() {
            val flow =
                when (state.value.selectedFilter) {
                    LaunchFilter.UPCOMING -> getUpcomingLaunches(state.value.pageSize, 0)
                    LaunchFilter.PACK -> getPastLaunches(state.value.pageSize, 0)
                }

            flow
                .onEach { result ->
                    result.fold(
                        onSuccess = { paginatedLaunches ->
                            println("DEBUG: Loaded ${paginatedLaunches.launches.size} launches")
                            paginatedLaunches.launches.take(3).forEach { launch ->
                                println("DEBUG: Launch ${launch.name} - Rocket ID: ${launch.rocketId}, Launchpad ID: ${launch.launchpadId}")
                            }

                            state.value
                                .copy(
                                    launches = paginatedLaunches.launches,
                                    isLoading = false,
                                    currentPage = 1,
                                    hasMoreItems = paginatedLaunches.hasMore,
                                    totalCount = paginatedLaunches.totalCount,
                                    errorMessage = null,
                                    rateLimitError = null,
                                ).sendToState()
                        },
                        onFailure = { error ->
                            state.value
                                .copy(
                                    isLoading = false,
                                    errorMessage = getErrorMessage(error),
                                    rateLimitError = null,
                                ).sendToState()
                            handleError(error)
                        },
                    )
                }.launchIn(viewModelScope)
        }

        fun formatLaunchDate(dateUtc: String): String =
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM/dd/yyyy", Locale.getDefault())
                val date = inputFormat.parse(dateUtc)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                try {
                    val inputFormatMs =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("MMM/dd/yyyy", Locale.getDefault())
                    val date = inputFormatMs.parse(dateUtc)
                    outputFormat.format(date ?: Date())
                } catch (e2: Exception) {
                    dateUtc.split("T")[0] // Fallback to just the date part
                }
            }

        fun formatLaunchTime(dateUtc: String): String =
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = inputFormat.parse(dateUtc)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                try {
                    val inputFormatMs =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val date = inputFormatMs.parse(dateUtc)
                    outputFormat.format(date ?: Date())
                } catch (e2: Exception) {
                    try {
                        dateUtc.split("T")[1].split(":")[0] + ":" + dateUtc.split("T")[1].split(":")[1] // Fallback to HH:mm
                    } catch (e3: Exception) {
                        "00:00"
                    }
                }
            }

        fun calculateCountdown(
            dateUtc: String,
            isUpcoming: Boolean,
        ): String {
            if (!isUpcoming) return "Completed"

            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val launchDate = inputFormat.parse(dateUtc) ?: return "TBD"
                val now = Date()
                val diffInMillis = launchDate.time - now.time

                if (diffInMillis <= 0) return "Launched"

                val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60

                when {
                    days > 0 -> "${days}d ${hours}h"
                    hours > 0 -> "${hours}h ${minutes}min"
                    else -> "${minutes}min"
                }
            } catch (e: Exception) {
                try {
                    val inputFormatMs =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val launchDate = inputFormatMs.parse(dateUtc) ?: return "TBD"
                    val now = Date()
                    val diffInMillis = launchDate.time - now.time

                    if (diffInMillis <= 0) return "Launched"

                    val days = TimeUnit.MILLISECONDS.toDays(diffInMillis)
                    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60

                    when {
                        days > 0 -> "${days}d ${hours}h"
                        hours > 0 -> "${hours}h ${minutes}min"
                        else -> "${minutes}min"
                    }
                } catch (e2: Exception) {
                    "TBD"
                }
            }
        }

        fun generateWeatherData(launchId: String): WeatherData {
            val hash = Math.abs(launchId.hashCode())
            val windSpeed = (30 + (hash % 40)).toString()
            val cloudCover = (10 + (hash % 50)).toString() + "%"
            val rainfall = if (hash % 10 < 2) "1.${hash % 10}mm" else "0.0mm"

            return WeatherData(
                windSpeed = "$windSpeed m/h",
                cloudCover = cloudCover,
                rainfall = rainfall,
            )
        }

        fun getRocketName(rocketId: String): String = state.value.allRockets[rocketId]?.name ?: "Unknown Rocket"

        fun getLaunchpadName(launchpadId: String): String = state.value.allLaunchpads[launchpadId]?.name ?: "Unknown Launchpad"

        private fun getErrorMessage(error: Throwable): String =
            when (error) {
                is HttpException.TooManyRequests -> {
                    "Rate limit exceeded."
                }
                is HttpException.NetworkError -> "Network error. Please check your connection."
                is HttpException.HttpError -> "Server error (${error.statusCode}). Please try again."
                else -> error.message ?: "An unexpected error occurred"
            }

        private fun handleError(error: Throwable) {
            when (error) {
                is HttpException.TooManyRequests -> {
                    val rateLimitError =
                        RateLimitError(
                            retryAfterSeconds = error.retryAfterSeconds,
                            message = getErrorMessage(error),
                        )
                    state.value
                        .copy(
                            rateLimitError = rateLimitError,
                            errorMessage = null, // Clear any existing error message
                        ).sendToState()
                    // Don't send ShowRateLimitError event to avoid duplicate messages
                }
                else -> {
                    state.value
                        .copy(
                            errorMessage = getErrorMessage(error),
                            rateLimitError = null, // Clear any existing rate limit error
                        ).sendToState()
                    HomeEvent.ShowError(getErrorMessage(error)).sendToEvent()
                }
            }
        }
    }

data class WeatherData(
    val windSpeed: String,
    val cloudCover: String,
    val rainfall: String,
)
