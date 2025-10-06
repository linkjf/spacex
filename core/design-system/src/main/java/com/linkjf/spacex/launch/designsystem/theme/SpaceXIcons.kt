package com.linkjf.spacex.launch.designsystem.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.linkjf.spacex.launch.designsystem.R

object SpaceXIcons {
    val Search: ImageVector = Icons.Default.Search
    val PlayVideo: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.ic_play_video)
    val Wind: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.ic_air_condition)
    val Clouds: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.ic_cloud_condition)
    val Rain: ImageVector
        @Composable get() = ImageVector.vectorResource(id = R.drawable.ic_rain_condition)
    val Sun: ImageVector = Icons.Default.WbSunny
}
