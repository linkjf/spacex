package com.linkjf.spacex.launch.home.presentation

import androidx.lifecycle.viewModelScope
import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetRocketsUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetLaunchpadsUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetRocketUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetLaunchpadUseCase
import com.linkjf.spacex.launch.mvi.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getUpcomingLaunches: GetUpcomingLaunchesUseCase,
        private val getPastLaunches: GetPastLaunchesUseCase,
        private val getRockets: GetRocketsUseCase,
        private val getLaunchpads: GetLaunchpadsUseCase,
        private val getRocket: GetRocketUseCase,
        private val getLaunchpad: GetLaunchpadUseCase,
    ) : StateViewModel<HomeState, HomeEvent, HomeAction>(HomeState()) {
        init {
            loadInitialData()
        }

        override fun reduce(action: HomeAction) {
            when (action) {
                // Navigation actions
                HomeAction.TapSettings -> HomeEvent.NavigateToSettings.sendToEvent()

                // Filter actions
                is HomeAction.SelectFilter -> selectFilter(action.filter)
                is HomeAction.SelectTab -> selectTab(action.tabIndex)

                // Refresh and loading actions
                HomeAction.PullToRefresh -> refresh()
                HomeAction.LoadMore -> loadMore()

                // Launch item actions
                is HomeAction.TapLaunch ->
                    HomeEvent
                        .NavigateToLaunchDetails(action.launchId)
                        .sendToEvent()

                is HomeAction.TapWatch -> handleWatchAction(action.launchId)

                // Error handling
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
                // TODO: Implement pagination logic
                // For now, just simulate loading more
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
                    // Fetch all rockets and launchpads first
                    val rocketsResult = getRockets()
                    val launchpadsResult = getLaunchpads()
                    
                    val allRockets = rocketsResult.getOrElse { emptyList() }.associateBy { it.id }
                    val allLaunchpads = launchpadsResult.getOrElse { emptyList() }.associateBy { it.id }
                    
                    // Debug logging
                    println("DEBUG: Fetched ${allRockets.size} rockets: ${allRockets.keys}")
                    println("DEBUG: Fetched ${allLaunchpads.size} launchpads: ${allLaunchpads.keys}")
                    
                    // Update state with all rockets and launchpads
                    state.value.copy(
                        allRockets = allRockets,
                        allLaunchpads = allLaunchpads
                    ).sendToState()
                    
                    // Now load launches
                    loadLaunches()
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
                            // Debug logging for launch data
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
                            
                            // Fetch individual rocket and launchpad data for each launch
                            loadIndividualRocketAndLaunchpadData(launches)
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

        private fun loadIndividualRocketAndLaunchpadData(launches: List<Launch>) {
            viewModelScope.launch {
                supervisorScope {
                    try {
                        // Collect unique rocket and launchpad IDs
                        val rocketIds = launches.map { it.rocketId }.distinct()
                        val launchpadIds = launches.map { it.launchpadId }.distinct()
                        
                        // Fetch rockets and launchpads in parallel
                        val rocketResults = rocketIds.map { rocketId ->
                            async {
                                try {
                                    val result = getRocket(rocketId)
                                    result.fold(
                                        onSuccess = { rocket -> 
                                            println("DEBUG: Successfully fetched rocket ${rocket.name} for ID $rocketId")
                                            rocketId to rocket.name
                                        },
                                        onFailure = { error ->
                                            println("DEBUG: Failed to fetch rocket $rocketId: ${error.message}")
                                            rocketId to getFallbackRocketName(rocketId)
                                        }
                                    )
                                } catch (e: Exception) {
                                    println("DEBUG: Exception fetching rocket $rocketId: ${e.message}")
                                    rocketId to getFallbackRocketName(rocketId)
                                }
                            }
                        }
                        
                        val launchpadResults = launchpadIds.map { launchpadId ->
                            async {
                                try {
                                    val result = getLaunchpad(launchpadId)
                                    result.fold(
                                        onSuccess = { launchpad -> 
                                            println("DEBUG: Successfully fetched launchpad ${launchpad.name} for ID $launchpadId")
                                            launchpadId to launchpad.name
                                        },
                                        onFailure = { error ->
                                            println("DEBUG: Failed to fetch launchpad $launchpadId: ${error.message}")
                                            launchpadId to getFallbackLaunchpadName(launchpadId)
                                        }
                                    )
                                } catch (e: Exception) {
                                    println("DEBUG: Exception fetching launchpad $launchpadId: ${e.message}")
                                    launchpadId to getFallbackLaunchpadName(launchpadId)
                                }
                            }
                        }
                        
                        // Wait for all requests to complete
                        val rocketNames = rocketResults.map { it.await() }.toMap()
                        val launchpadNames = launchpadResults.map { it.await() }.toMap()
                        
                        // Update state with the fetched names
                        state.value.copy(
                            rocketNames = rocketNames,
                            launchpadNames = launchpadNames
                        ).sendToState()
                        
                    } catch (e: Exception) {
                        println("DEBUG: Exception in loadIndividualRocketAndLaunchpadData: ${e.message}")
                    }
                }
            }
        }

        private fun getFallbackRocketName(rocketId: String): String {
            return when {
                rocketId.contains("falcon1", ignoreCase = true) -> "Falcon 1"
                rocketId.contains("falcon9", ignoreCase = true) -> "Falcon 9"
                rocketId.contains("falconheavy", ignoreCase = true) -> "Falcon Heavy"
                rocketId.contains("starship", ignoreCase = true) -> "Starship"
                rocketId.contains("falcon", ignoreCase = true) -> "Falcon 9"
                else -> "Unknown Rocket"
            }
        }

        private fun getFallbackLaunchpadName(launchpadId: String): String {
            return when {
                launchpadId.contains("ksc", ignoreCase = true) -> "KSC LC-39A"
                launchpadId.contains("slc", ignoreCase = true) -> "SLC-40"
                launchpadId.contains("kwajalein", ignoreCase = true) -> "Kwajalein Atoll"
                launchpadId.contains("lcs", ignoreCase = true) -> "LCS-421"
                else -> "Unknown Launchpad"
            }
        }

        // Helper functions for data formatting
        fun formatLaunchDate(dateUtc: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("MMM/dd/yyyy   HH:mm", Locale.getDefault())
                val date = inputFormat.parse(dateUtc)
                outputFormat.format(date ?: Date())
            } catch (e: Exception) {
                dateUtc // Return original if parsing fails
            }
        }

        fun calculateCountdown(dateUtc: String, isUpcoming: Boolean): String {
            if (!isUpcoming) return "Completed"
            
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
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
                "TBD"
            }
        }

        fun generateWeatherData(launchId: String): WeatherData {
            // Generate consistent pseudo-random weather data based on launch ID
            val hash = Math.abs(launchId.hashCode()) // Ensure positive hash
            val windSpeed = (30 + (hash % 40)).toString() // 30-70 m/h
            val cloudCover = (10 + (hash % 50)).toString() + "%" // 10-60% (always positive)
            val rainfall = if (hash % 10 < 2) "1.${hash % 10}mm" else "0.0mm" // Mostly 0.0mm, occasionally some rain
            
            return WeatherData(
                windSpeed = "${windSpeed} m/h",
                cloudCover = cloudCover,
                rainfall = rainfall
            )
        }

        fun getRocketName(rocketId: String): String {
            return state.value.rocketNames[rocketId] ?: "Loading..."
        }

        fun getLaunchpadName(launchpadId: String): String {
            return state.value.launchpadNames[launchpadId] ?: "Loading..."
        }
    }

    data class WeatherData(
        val windSpeed: String,
        val cloudCover: String,
        val rainfall: String
    )
