package com.linkjf.spacex.launch.design_system.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

object SpaceXSpacing {
    // Base spacing unit (4dp)
    private const val BaseUnit = 4
    
    // Micro spacing
    val Micro: Dp = 2.dp
    val Micro2: Dp = 4.dp
    
    // Small spacing
    val Small: Dp = 8.dp
    val Small2: Dp = 12.dp
    val Small3: Dp = 16.dp
    
    // Medium spacing
    val Medium: Dp = 20.dp
    val Medium2: Dp = 24.dp
    val Medium3: Dp = 28.dp
    
    // Large spacing
    val Large: Dp = 32.dp
    val Large2: Dp = 36.dp
    val Large3: Dp = 40.dp
    
    // Extra large spacing
    val ExtraLarge: Dp = 48.dp
    val ExtraLarge2: Dp = 56.dp
    val ExtraLarge3: Dp = 64.dp
    
    // Component specific spacing
    val CardPadding: Dp = Small3 // 16dp
    val CardMargin: Dp = Medium // 20dp
    val TabPadding: Dp = Small3 // 16dp
    val TabMargin: Dp = Small2 // 12dp
    val HeaderPadding: Dp = Medium // 20dp
    val HeaderMargin: Dp = Medium // 20dp
    
    // Border radius
    val BorderRadiusSmall: Dp = Small2 // 12dp
    val BorderRadiusMedium: Dp = Small3 // 16dp
    val BorderRadiusLarge: Dp = Medium2 // 24dp
    
    // Icon sizes
    val IconSmall: Dp = 16.dp
    val IconMedium: Dp = 24.dp
    val IconLarge: Dp = 32.dp
    
    // Time unit display
    val TimeUnitBox: Dp = 60.dp
    
    // Progress indicator
    val ProgressIndicatorSize: Dp = 80.dp
    val ProgressIndicatorStroke: Dp = 6.dp
    
    // Divider thickness
    val DividerThickness: Dp = 1.dp
    val DividerThicknessDefault: Float = 1.4f
    
    // Touch targets
    val TouchTarget: Dp = 48.dp
    val TouchTargetSmall: Dp = 40.dp
    
    // Interactive card specific
    val LaunchImageSize: Dp = 78.dp
    val InteractiveCardPressedScale: Float = 0.98f
    val InteractiveCardAnimationDuration: Int = 100
    
    // Interactive tab specific
    val InteractiveTabAnimationDuration: Int = 200
    
    // Launch details specific
    val LaunchDetailsImageSize: Dp = 96.dp
    val LaunchDetailsDateAlpha: Float = 0.7f
    
    // Launch image specific
    val LaunchImageDefaultSize: Dp = 78.dp
    val ProgressIndicatorStrokeWidth: Dp = 2.dp
    
    // Pagination specific
    val PaginationLoadMoreThreshold: Int = 3
    
    // Weather specific
    val WeatherDividerThickness: Dp = 1.dp
    val WeatherProgressIndicatorHeight: Dp = 4.dp
    val WeatherProgressIndicatorRadius: Dp = 2.dp
    
    // Weather calculation constants
    val WeatherWindSpeedMax: Float = 50f
    val WeatherCloudCoverMax: Float = 100f
    val WeatherRainfallMax: Float = 10f
    val WeatherTemperatureOffset: Float = 20f
    val WeatherTemperatureRange: Float = 60f
    val WeatherHumidityMax: Float = 100f
    val WeatherVisibilityMax: Float = 20f
}
