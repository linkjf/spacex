package com.linkjf.spacex.launch.navigation

/**
 * Sealed class representing all possible navigation routes in the app
 */
sealed class Screen(
    val route: String,
) {
    data object Home : Screen("home")
    data object LaunchDetails : Screen("launch_details?launchData={launchData}")

    // Future screens can be added here
    // data object Settings : Screen("settings")
}
