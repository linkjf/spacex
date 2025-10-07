package com.linkjf.spacex.launch.detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.linkjf.spacex.launch.core.time.TimeUtils
import com.linkjf.spacex.launch.core.weather.WeatherDataGenerator
import com.linkjf.spacex.launch.mvi.StateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : StateViewModel<DetailState, DetailEvent, DetailAction>(DetailState()) {
        private val launchId: String = run {
            val launchDataJson = savedStateHandle["launchData"] ?: ""
            try {
                val launchData = Json.decodeFromString<LaunchDetailData>(launchDataJson)
                launchData.id
            } catch (e: Exception) {
                "unknown"
            }
        }

        init {
            loadLaunchDetails()
        }

        companion object {
            private const val COUNTDOWN_UPDATE_INTERVAL_MS = 1000L
        }

        override fun reduce(action: DetailAction) {
            when (action) {
                DetailAction.OnBackClicked -> DetailEvent.NavigateBack.sendToEvent()
                DetailAction.OnMenuClicked -> DetailEvent.ShowMenu.sendToEvent()
                DetailAction.OnWatchClicked -> handleWatchAction()
                DetailAction.OnRefresh -> loadLaunchDetails()
                DetailAction.DismissError -> dismissError()
                DetailAction.DismissRateLimitError -> dismissRateLimitError()
            }
        }

        fun loadLaunchDetails(launchData: LaunchDetailData? = null) {
            viewModelScope.launch {
                state.value.copy(isLoading = true, errorMessage = null, rateLimitError = null).sendToState()

                // Simulate loading delay
                delay(1000)

                try {
                    // Use provided launch data or fall back to mock data
                    val weatherData = WeatherDataGenerator.generateWeatherData(launchId)
                    
                    if (launchData != null) {
                        // Use real launch data from navigation
                        state.value
                            .copy(
                                isLoading = false,
                                launchId = launchData.id,
                                launchName = launchData.name,
                                launchCode = "${launchData.rocketId} - ${launchData.launchpadId}",
                                dateUtc = "${launchData.date}T${launchData.time}Z",
                                patchImageUrl = launchData.patchImageUrl,
                                status = LaunchDetailStatus.SCHEDULED,
                                isLive = false,
                                rocketName = launchData.rocketId,
                                launchpadName = launchData.launchpadId,
                                location = "Cape Canaveral Florida", // Default location
                                details = "Launch details for ${launchData.name}",
                                webcastUrl = "https://youtube.com/watch?v=example",
                                weatherData =
                                    WeatherDetailData(
                                        windSpeed = weatherData.windSpeed,
                                        windGust = weatherData.windGust,
                                        cloudCover = weatherData.cloudCover,
                                        precipitationProbability = weatherData.precipitationProb,
                                        visibility = weatherData.visibility,
                                    ),
                                errorMessage = null,
                                rateLimitError = null,
                            ).sendToState()
                    } else {
                        // Use mock data for UI development
                        state.value
                            .copy(
                                isLoading = false,
                                launchId = launchId,
                                launchName = "Starlink Group 2-38",
                                launchCode = "EU89 - LCA827",
                                dateUtc = "2025-10-04T10:00:00Z",
                                patchImageUrl = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                                status = LaunchDetailStatus.SCHEDULED,
                                isLive = false,
                                rocketName = "Falcon 9",
                                launchpadName = "Space Launch Complex 40",
                                location = "Cape Canaveral Florida",
                                details = "A batch of Starlink satellites for global internet coverage.",
                                webcastUrl = "https://youtube.com/watch?v=example",
                                weatherData =
                                    WeatherDetailData(
                                        windSpeed = weatherData.windSpeed,
                                        windGust = weatherData.windGust,
                                        cloudCover = weatherData.cloudCover,
                                        precipitationProbability = weatherData.precipitationProb,
                                        visibility = weatherData.visibility,
                                    ),
                                errorMessage = null,
                                rateLimitError = null,
                            ).sendToState()
                    }
                } catch (e: Exception) {
                    state.value
                        .copy(
                            isLoading = false,
                            errorMessage = e.message ?: "Failed to load launch details",
                        ).sendToState()
                }
            }
        }

        private fun handleWatchAction() {
            val url = state.value.webcastUrl
            if (url != null) {
                DetailEvent.NavigateToWebcast(url).sendToEvent()
            } else {
                state.value
                    .copy(errorMessage = "Webcast not available")
                    .sendToState()
            }
        }

        private fun dismissError() {
            state.value.copy(errorMessage = null).sendToState()
        }

        private fun dismissRateLimitError() {
            state.value.copy(rateLimitError = null).sendToState()
        }
    }

