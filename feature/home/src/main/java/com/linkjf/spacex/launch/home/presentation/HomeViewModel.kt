package com.linkjf.spacex.launch.home.presentation

import androidx.lifecycle.viewModelScope
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import com.linkjf.spacex.launch.mvi.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
                HomeAction.Retry -> retry()
            }
        }

        private fun loadInitialData() {
            if (state.value.launches.isNotEmpty()) return

            state.value.copy(isLoading = true).sendToState()
            loadAllData()
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
            if (state.value.isLoadingMore || state.value.launches.isEmpty()) return

            state.value.copy(isLoadingMore = true).sendToState()

            viewModelScope.launch {
                kotlinx.coroutines.delay(1000)
                state.value.copy(isLoadingMore = false).sendToState()
            }
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

        private fun retry() {
            dismissError()
            loadInitialData()
        }

        private fun loadAllData() {
            viewModelScope.launch {
                try {
                    val upcomingResult = getUpcomingLaunches()
                    val pastResult = getPastLaunches()

                    val upcomingLaunches = upcomingResult.getOrElse { emptyList() }
                    val pastLaunches = pastResult.getOrElse { emptyList() }

                    val allRockets = mutableMapOf<String, Rocket>()
                    val allLaunchpads = mutableMapOf<String, Launchpad>()

                    (upcomingLaunches + pastLaunches).forEach { launch ->
                        launch.rocket?.let { rocket ->
                            allRockets[rocket.id] = rocket
                        }
                        launch.launchpad?.let { launchpad ->
                            allLaunchpads[launchpad.id] = launchpad
                        }
                    }

                    println("DEBUG: Extracted ${allRockets.size} rockets from embedded data: ${allRockets.keys}")
                    println("DEBUG: Extracted ${allLaunchpads.size} launchpads from embedded data: ${allLaunchpads.keys}")

                    state.value
                        .copy(
                            allRockets = allRockets,
                            allLaunchpads = allLaunchpads,
                        ).sendToState()

                    val launchesToShow =
                        when (state.value.selectedFilter) {
                            LaunchFilter.UPCOMING -> upcomingLaunches
                            LaunchFilter.PACK -> pastLaunches
                        }

                    println("DEBUG: Loaded ${launchesToShow.size} launches for ${state.value.selectedFilter}")
                    launchesToShow.take(3).forEach { launch ->
                        println("DEBUG: Launch ${launch.name} - Rocket ID: ${launch.rocketId}, Launchpad ID: ${launch.launchpadId}")
                    }

                    state.value
                        .copy(
                            launches = launchesToShow,
                            isLoading = false,
                            errorMessage = null,
                        ).sendToState()
                } catch (e: Exception) {
                    state.value
                        .copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Failed to load initial data",
                        ).sendToState()
                    HomeEvent.ShowError(e.message ?: "Failed to load initial data").sendToEvent()
                }
            }
        }

        private fun loadLaunches() {
            viewModelScope.launch {
                try {
                    val result =
                        when (state.value.selectedFilter) {
                            LaunchFilter.UPCOMING -> getUpcomingLaunches()
                            LaunchFilter.PACK -> getPastLaunches()
                        }

                    result.fold(
                        onSuccess = { launches ->

                            println("DEBUG: Loaded ${launches.size} launches")
                            launches.take(3).forEach { launch ->
                                println("DEBUG: Launch ${launch.name} - Rocket ID: ${launch.rocketId}, Launchpad ID: ${launch.launchpadId}")
                            }

                            state.value
                                .copy(
                                    launches = launches,
                                    isLoading = false,
                                    errorMessage = null,
                                ).sendToState()
                        },
                        onFailure = { error ->
                            state.value
                                .copy(
                                    isLoading = false,
                                    errorMessage = error.message ?: "Failed to load launches",
                                ).sendToState()
                            HomeEvent
                                .ShowError(error.message ?: "Failed to load launches")
                                .sendToEvent()
                        },
                    )
                } catch (e: Exception) {
                    state.value
                        .copy(
                            isLoading = false,
                            errorMessage = e.message ?: "An unexpected error occurred",
                        ).sendToState()
                    HomeEvent.ShowError(e.message ?: "An unexpected error occurred").sendToEvent()
                }
            }
        }

        fun formatLaunchDate(dateUtc: String): String =
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM/dd/yyyy   HH:mm", Locale.getDefault())
                val date = inputFormat.parse(dateUtc)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                try {
                    val inputFormatMs =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("MMM/dd/yyyy   HH:mm", Locale.getDefault())
                    val date = inputFormatMs.parse(dateUtc)
                    outputFormat.format(date ?: Date())
                } catch (e2: Exception) {
                    dateUtc
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
    }

data class WeatherData(
    val windSpeed: String,
    val cloudCover: String,
    val rainfall: String,
)
