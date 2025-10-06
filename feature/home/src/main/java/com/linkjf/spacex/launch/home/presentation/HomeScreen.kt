package com.linkjf.spacex.launch.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.linkjf.spacex.launch.designsystem.components.SpaceXErrorCard
import com.linkjf.spacex.launch.designsystem.components.SpaceXLaunchListWithPaging3
import com.linkjf.spacex.launch.designsystem.components.SpaceXRateLimitCard
import com.linkjf.spacex.launch.designsystem.components.SpaceXScreenHeader
import com.linkjf.spacex.launch.designsystem.components.SpaceXTabSelector
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
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

    val upcomingLaunchesPagingItems = viewModel.upcomingLaunches.collectAsLazyPagingItems()
    val pastLaunchesPagingItems = viewModel.pastLaunches.collectAsLazyPagingItems()

    val currentPagingItems =
        if (state.selectedTabIndex == 0) {
            upcomingLaunchesPagingItems
        } else {
            pastLaunchesPagingItems
        }

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

        state.rateLimitError?.let { rateLimitError ->
            SpaceXRateLimitCard(
                message = rateLimitError.message,
                retryAfterSeconds = rateLimitError.retryAfterSeconds,
                onDismiss = { viewModel.reduce(HomeAction.DismissRateLimitError) },
            )
        }

        state.errorMessage?.let { errorMessage ->
            SpaceXErrorCard(
                message = errorMessage,
                onDismiss = { viewModel.reduce(HomeAction.DismissError) },
            )
        }

        SpaceXLaunchListWithPaging3(
            lazyPagingItems = currentPagingItems,
            onLaunchClick = { launchItem ->
                viewModel.reduce(HomeAction.TapLaunch(launchItem.id))
            },
            onWatchClick = { launchItem ->
                viewModel.reduce(HomeAction.TapWatch(launchItem.id))
            },
            emptyMessage =
                if (state.selectedTabIndex == 0) {
                    "No upcoming launches available"
                } else {
                    "No past launches available"
                },
        )
    }
}
