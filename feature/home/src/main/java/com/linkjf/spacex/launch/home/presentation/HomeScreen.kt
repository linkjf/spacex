package com.linkjf.spacex.launch.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.linkjf.spacex.launch.core.time.TimeUtils
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.designsystem.components.SpaceXErrorCard
import com.linkjf.spacex.launch.designsystem.components.SpaceXLaunchListWithPaging3
import com.linkjf.spacex.launch.designsystem.components.SpaceXRateLimitCard
import com.linkjf.spacex.launch.designsystem.components.SpaceXScreenHeader
import com.linkjf.spacex.launch.designsystem.components.SpaceXTabSelector
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.home.R
import com.linkjf.spacex.launch.mvi.EventEffect

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit = {},
    onLaunchClick: (LaunchListItem) -> Unit = {},
    onWatchClick: (String) -> Unit = {},
    onOpenWebcast: (String) -> Unit = {},
    onOpenArticle: (String) -> Unit = {},
    onOpenWikipedia: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    val currentPagingItems =
        if (state.selectedTabIndex == 0) {
            viewModel.upcomingLaunches.collectAsLazyPagingItems()
        } else {
            viewModel.pastLaunches.collectAsLazyPagingItems()
        }

    EventEffect(flow = viewModel.event) { event ->
        when (event) {
            HomeEvent.NavigateToSettings -> onSettingsClick()
            is HomeEvent.NavigateToLaunchDetails -> onLaunchClick(event.launchItem)
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
            title = stringResource(R.string.home_screen_title),
            onSearchClick = { viewModel.reduce(HomeAction.TapSettings) },
            searchContentDescription = stringResource(R.string.settings_content_description),
        )

        SpaceXTabSelector(
            tabs =
                listOf(
                    stringResource(R.string.tab_label_upcoming),
                    stringResource(R.string.tab_label_past),
                ),
            selectedIndex = state.selectedTabIndex,
            onTabSelected = { index ->
                viewModel.reduce(HomeAction.SelectTab(index))
            },
        )

        state.rateLimitError?.let { rateLimitError ->
            SpaceXRateLimitCard(
                message =
                    stringResource(
                        R.string.rate_limit_message,
                        TimeUtils.formatDuration(rateLimitError.retryAfterSeconds ?: 0),
                    ),
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
                viewModel.reduce(HomeAction.TapLaunch(launchItem))
            },
            onWatchClick = { launchItem ->
                viewModel.reduce(HomeAction.TapWatch(launchItem.id))
            },
            emptyMessage =
                if (state.selectedTabIndex == HomeState.TAB_INDEX_UPCOMING) {
                    stringResource(R.string.empty_message_upcoming)
                } else {
                    stringResource(R.string.empty_message_past)
                },
        )
    }
}
