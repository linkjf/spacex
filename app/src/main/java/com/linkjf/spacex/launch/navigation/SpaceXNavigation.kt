package com.linkjf.spacex.launch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.linkjf.spacex.launch.detail.presentation.DetailScreen
import com.linkjf.spacex.launch.detail.presentation.LaunchDetailData
import com.linkjf.spacex.launch.home.presentation.HomeScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Main navigation composable that defines all routes and their destinations
 */
@Composable
fun SpaceXNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onSettingsClick = {
                    // TODO: Navigate to settings when implemented
                },
                onLaunchClick = { launchItem ->
                    navController.navigateToLaunchDetails(launchItem)
                },
                onWatchClick = { launchId ->
                    // TODO: Handle watch action when implemented
                },
                onOpenWebcast = { url ->
                    // TODO: Open webcast URL
                },
                onOpenArticle = { url ->
                    // TODO: Open article URL
                },
                onOpenWikipedia = { url ->
                    // TODO: Open Wikipedia URL
                },
            )
        }

        composable(
            route = Screen.LaunchDetails.route,
            arguments = listOf(
                navArgument("launchData") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val launchDataJson = backStackEntry.arguments?.getString("launchData") ?: ""
            val launchData = try {
                Json.decodeFromString<LaunchDetailData>(launchDataJson)
            } catch (e: Exception) {
                LaunchDetailData(
                    id = "unknown",
                    name = "Unknown Launch",
                    date = "",
                    time = "",
                    rocketId = "",
                    launchpadId = "",
                )
            }
            
            DetailScreen(
                launchData = launchData,
                onBackClick = {
                    navController.popBackStack()
                },
                onWatchWebcast = { url ->
                    // TODO: Open webcast URL
                }
            )
        }

        // Future routes can be added here
        // composable(Screen.Settings.route) { SettingsScreen() }
    }
}
