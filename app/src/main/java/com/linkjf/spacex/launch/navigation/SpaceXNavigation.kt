package com.linkjf.spacex.launch.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.linkjf.spacex.launch.home.presentation.HomeScreen

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
                onLaunchClick = { launchId ->
                    // TODO: Navigate to launch details when implemented
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

        // Future routes can be added here
        // composable(Screen.Settings.route) { SettingsScreen() }
        // composable(
        //     route = Screen.LaunchDetails.route,
        //     arguments = listOf(navArgument("launchId") { type = NavType.StringType })
        // ) { backStackEntry ->
        //     val launchId = backStackEntry.arguments?.getString("launchId") ?: ""
        //     LaunchDetailsScreen(launchId = launchId)
        // }
    }
}
