package com.linkjf.spacex.launch.navigation

import androidx.navigation.NavController
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.detail.presentation.LaunchDetailData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Extension function to navigate to launch details with launch data
 */
fun NavController.navigateToLaunchDetails(launchItem: LaunchListItem) {
    val launchData = LaunchDetailData(
        id = launchItem.id,
        name = launchItem.name,
        date = launchItem.date,
        time = launchItem.time,
        rocketId = launchItem.rocketId,
        launchpadId = launchItem.launchpadId,
        patchImageUrl = launchItem.patchImageUrl,
        windSpeed = launchItem.windSpeed,
        cloudCover = launchItem.cloudCover,
        rainfall = launchItem.rainfall,
        countdown = launchItem.countdown,
        isUpcoming = launchItem.isUpcoming,
    )
    
    val launchDataJson = Json.encodeToString(launchData)
    
    navigate("${Screen.LaunchDetails.route}?launchData=$launchDataJson")
}
