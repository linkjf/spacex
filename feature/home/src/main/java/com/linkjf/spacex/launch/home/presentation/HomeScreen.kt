package com.linkjf.spacex.launch.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.designsystem.components.SpaceXLaunchList
import com.linkjf.spacex.launch.designsystem.components.SpaceXScreenHeader
import com.linkjf.spacex.launch.designsystem.components.SpaceXTabSelector
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.mvi.EventEffect

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
    onLaunchClick: (String) -> Unit = {},
    onWatchClick: (String) -> Unit = {},
    onOpenWebcast: (String) -> Unit = {},
    onOpenArticle: (String) -> Unit = {},
    onOpenWikipedia: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    EventEffect(flow = viewModel.event) { event ->
        when (event) {
            HomeEvent.NavigateToSettings -> onSettingsClick()
            is HomeEvent.NavigateToLaunchDetails -> onLaunchClick(event.launchId)
            is HomeEvent.OpenWebcast -> onOpenWebcast(event.url)
            is HomeEvent.OpenArticle -> onOpenArticle(event.url)
            is HomeEvent.OpenWikipedia -> onOpenWikipedia(event.url)
            else -> {}
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
    ) {
        SpaceXScreenHeader(
            title = "Launches",
            onSearchClick = { viewModel.reduce(HomeAction.TapSettings) },
            searchContentDescription = "Settings",
        )

        SpaceXTabSelector(
            tabs = listOf("Upcoming", "Pack"),
            selectedIndex = state.selectedTabIndex,
            onTabSelected = { index ->
                viewModel.reduce(HomeAction.SelectTab(index))
            },
        )

            SpaceXLaunchList(
                launches =
                    state.launches.map { launch ->
                        val weatherData = viewModel.generateWeatherData(launch.id)
                        val rocketName = viewModel.getRocketName(launch.rocketId)
                        val launchpadName = viewModel.getLaunchpadName(launch.launchpadId)
                        val formattedDate = viewModel.formatLaunchDate(launch.dateUtc)
                        val countdown = viewModel.calculateCountdown(launch.dateUtc, launch.upcoming)
                        
                        LaunchListItem(
                            id = launch.id,
                            name = launch.name,
                            dateUtc = formattedDate,
                            rocketId = rocketName,
                            launchpadId = launchpadName,
                            patchImageUrl = launch.links?.patch?.small,
                            windSpeed = weatherData.windSpeed,
                            cloudCover = weatherData.cloudCover,
                            rainfall = weatherData.rainfall,
                            countdown = countdown,
                            isUpcoming = launch.upcoming,
                        )
                    },
            onLaunchClick = { launch -> viewModel.reduce(HomeAction.TapLaunch(launch.id)) },
            onWatchClick = { launch -> viewModel.reduce(HomeAction.TapWatch(launch.id)) },
            isLoading = state.isLoading,
            isEmpty = state.launches.isEmpty() && !state.isLoading,
            emptyMessage = "No launches available",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun HomeScreenPreview() {
    SpaceXTheme {
        HomeScreenPreviewContent(
            launches =
                listOf(
                    LaunchListItem(
                        id = "launch_1",
                        name = "Starlink Group 2-38",
                        dateUtc = "Oct/4/2025     10:00",
                        rocketId = "Falcon 4",
                        launchpadId = "LCS-421",
                        patchImageUrl = null,
                        windSpeed = "55 m/h",
                        cloudCover = "35%",
                        rainfall = "0.0mm",
                        countdown = "89min",
                        isUpcoming = true,
                    ),
                    LaunchListItem(
                        id = "launch_2",
                        name = "Starlink Group 2-39",
                        dateUtc = "Oct/6/2025     14:30",
                        rocketId = "Falcon 9",
                        launchpadId = "SLC-40",
                        patchImageUrl = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                        windSpeed = "45 m/h",
                        cloudCover = "20%",
                        rainfall = "0.0mm",
                        countdown = "2h 15min",
                        isUpcoming = true,
                    ),
                    LaunchListItem(
                        id = "launch_3",
                        name = "Falcon Heavy Demo",
                        dateUtc = "Oct/8/2025     16:00",
                        rocketId = "Falcon Heavy",
                        launchpadId = "KSC LC-39A",
                        patchImageUrl = null,
                        windSpeed = "60 m/h",
                        cloudCover = "40%",
                        rainfall = "1.2mm",
                        countdown = "3d 2h",
                        isUpcoming = true,
                    ),
                ),
            selectedTabIndex = 0,
            isLoading = false,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun HomeScreenPackTabPreview() {
    SpaceXTheme {
        HomeScreenPreviewContent(
            launches =
                listOf(
                    LaunchListItem(
                        id = "launch_4",
                        name = "Starlink Group 1-45",
                        dateUtc = "Sep/28/2025   12:00",
                        rocketId = "Falcon 9",
                        launchpadId = "SLC-40",
                        patchImageUrl = "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png",
                        windSpeed = "30 m/h",
                        cloudCover = "15%",
                        rainfall = "0.0mm",
                        countdown = "Completed",
                        isUpcoming = false,
                    ),
                ),
            selectedTabIndex = 1,
            isLoading = false,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun HomeScreenLoadingPreview() {
    SpaceXTheme {
        HomeScreenPreviewContent(
            launches = emptyList(),
            selectedTabIndex = 0,
            isLoading = true,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun HomeScreenEmptyPreview() {
    SpaceXTheme {
        HomeScreenPreviewContent(
            launches = emptyList(),
            selectedTabIndex = 0,
            isLoading = false,
        )
    }
}

@Composable
private fun HomeScreenPreviewContent(
    modifier: Modifier = Modifier,
    launches: List<LaunchListItem>,
    selectedTabIndex: Int,
    isLoading: Boolean,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
    ) {
        SpaceXScreenHeader(
            title = "Launches",
            onSearchClick = { },
            searchContentDescription = "Settings",
        )

        SpaceXTabSelector(
            tabs = listOf("Upcoming", "Pack"),
            selectedIndex = selectedTabIndex,
            onTabSelected = { },
        )

        SpaceXLaunchList(
            launches = launches,
            onLaunchClick = { },
            onWatchClick = { },
            isLoading = isLoading,
            isEmpty = launches.isEmpty() && !isLoading,
            emptyMessage = "No launches available",
        )
    }
}
