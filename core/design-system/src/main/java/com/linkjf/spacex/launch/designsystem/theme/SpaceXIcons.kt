package com.linkjf.spacex.launch.designsystem.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

object SpaceXIcons {
    // Navigation Icons
    val Search: ImageVector = Icons.Default.Search
    val PlayCircle: ImageVector = Icons.Default.PlayCircle

    // Weather Icons
    val Wind: ImageVector = Icons.Default.Air
    val Clouds: ImageVector = Icons.Default.Cloud
    val Rain: ImageVector = Icons.Default.WaterDrop
    val Sun: ImageVector = Icons.Default.WbSunny

    // Custom Icons (to be added when we have custom vector assets)
    // val SpaceXLogo: ImageVector = ...
    // val Rocket: ImageVector = ...
    // val LaunchPad: ImageVector = ...
}
