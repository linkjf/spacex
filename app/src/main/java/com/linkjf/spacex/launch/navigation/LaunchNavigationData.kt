package com.linkjf.spacex.launch.navigation

import kotlinx.serialization.Serializable

/**
 * Data class representing launch information passed between screens via navigation
 */
@Serializable
data class LaunchNavigationData(
    val id: String,
    val name: String,
    val date: String,
    val time: String,
    val rocketId: String,
    val launchpadId: String,
    val patchImageUrl: String? = null,
    val windSpeed: String = "60%",
    val cloudCover: String = "35%",
    val rainfall: String = "0.0mm",
    val countdown: String = "89min",
    val isUpcoming: Boolean = true,
)
