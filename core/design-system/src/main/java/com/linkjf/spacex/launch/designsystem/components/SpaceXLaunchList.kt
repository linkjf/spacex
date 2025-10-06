package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXIcons
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography

data class LaunchListItem(
    val id: String,
    val name: String,
    val date: String,
    val time: String,
    val rocketId: String,
    val launchpadId: String,
    val patchImageUrl: String? = null,
    val windSpeed: String = "60%",
    val cloudCover: String = "35%",
    val rainfall: String = "0.0mm",
    val countdown: String = "89min",
    val isUpcoming: Boolean = true,
) {
    // Override equals and hashCode for better performance in LazyColumn
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LaunchListItem) return false

        return id == other.id &&
            name == other.name &&
            date == other.date &&
            time == other.time &&
            rocketId == other.rocketId &&
            launchpadId == other.launchpadId &&
            patchImageUrl == other.patchImageUrl &&
            windSpeed == other.windSpeed &&
            cloudCover == other.cloudCover &&
            rainfall == other.rainfall &&
            countdown == other.countdown &&
            isUpcoming == other.isUpcoming
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + rocketId.hashCode()
        result = 31 * result + launchpadId.hashCode()
        result = 31 * result + (patchImageUrl?.hashCode() ?: 0)
        result = 31 * result + windSpeed.hashCode()
        result = 31 * result + cloudCover.hashCode()
        result = 31 * result + rainfall.hashCode()
        result = 31 * result + countdown.hashCode()
        result = 31 * result + isUpcoming.hashCode()
        return result
    }
}

data class LaunchListError(
    val message: String,
    val retryAction: (() -> Unit)? = null,
)

data class PaginationConfig(
    val isLoadingMore: Boolean = false,
    val hasMoreItems: Boolean = true,
    val onLoadMore: () -> Unit,
)

@Composable
fun SpaceXLaunchList(
    launches: List<LaunchListItem>,
    onLaunchClick: (LaunchListItem) -> Unit,
    onWatchClick: (LaunchListItem) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    emptyMessage: String = "No launches available",
    loadingColor: Color = SpaceXColors.Primary,
    textColor: Color = SpaceXColors.OnSurface,
    contentPadding: PaddingValues = PaddingValues(SpaceXSpacing.HeaderPadding),
) {
    val listState = rememberLazyListState()

    when {
        isLoading -> {
            SpaceXLoadingState(
                modifier = modifier,
                loadingColor = loadingColor,
            )
        }

        isEmpty -> {
            SpaceXEmptyState(
                message = emptyMessage,
                modifier = modifier,
                textColor = textColor,
            )
        }

        else -> {
            // Memoize the LaunchData conversion to avoid recomposition
            val launchDataList =
                remember(launches) {
                    launches.map { launch ->
                        LaunchData(
                            id = launch.id,
                            name = launch.name,
                            date = launch.date,
                            time = launch.time,
                            rocketId = launch.rocketId,
                            launchpadId = launch.launchpadId,
                            patchImageUrl = launch.patchImageUrl,
                            windSpeed = launch.windSpeed,
                            cloudCover = launch.cloudCover,
                            rainfall = launch.rainfall,
                            countdown = launch.countdown,
                        )
                    }
                }

            LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.CardMargin),
                contentPadding = contentPadding,
            ) {
                itemsIndexed(
                    items = launchDataList,
                    key = { _, launchData -> launchData.id },
                    contentType = { _, _ -> "launch_card" },
                ) { index, launchData ->
                    SpaceXLaunchCard(
                        launch = launchData,
                        onWatchClick = { onWatchClick(launches[index]) },
                        onClick = { onLaunchClick(launches[index]) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceXLoadingState(
    modifier: Modifier = Modifier,
    loadingColor: Color = SpaceXColors.Primary,
    message: String = "Loading launches...",
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
        ) {
            CircularProgressIndicator(
                color = loadingColor,
                modifier = Modifier.padding(SpaceXSpacing.Medium),
            )

            Text(
                text = message,
                style = SpaceXTypography.Typography.bodyLarge,
                color = SpaceXColors.OnSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun SpaceXEmptyState(
    message: String,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurfaceVariant,
    icon: @Composable (() -> Unit)? = null,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
        ) {
            icon?.invoke()

            Text(
                text = message,
                style = SpaceXTypography.Typography.titleMedium,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(SpaceXSpacing.HeaderPadding),
            )
        }
    }
}

@Composable
fun SpaceXErrorState(
    error: LaunchListError,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
    textColor: Color = SpaceXColors.Error,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
        ) {
            Text(
                text = "⚠️",
                style = SpaceXTypography.Typography.displayLarge,
            )

            Text(
                text = error.message,
                style = SpaceXTypography.Typography.titleMedium,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(SpaceXSpacing.HeaderPadding),
            )

            if (error.retryAction != null && onRetry != null) {
                SpaceXIconButton(
                    onClick = onRetry,
                    icon = SpaceXIcons.Search,
                    contentDescription = "Retry",
                    iconColor = SpaceXColors.Primary,
                    backgroundColor = SpaceXColors.SurfaceVariant,
                    modifier = Modifier.padding(SpaceXSpacing.Medium),
                )
            }
        }
    }
}

@Composable
fun SpaceXLaunchListWithRefresh(
    launches: List<LaunchListItem>,
    onLaunchClick: (LaunchListItem) -> Unit,
    onWatchClick: (LaunchListItem) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isRefreshing: Boolean = false,
    isEmpty: Boolean = false,
    emptyMessage: String = "No launches available",
) {
    val listState = rememberLazyListState()

    LaunchedEffect(isRefreshing) {
        isRefreshing.not().let { onRefresh() }
    }

    when {
        isLoading -> {
            SpaceXLoadingState(
                modifier = modifier,
                loadingColor = SpaceXColors.Primary,
            )
        }

        isEmpty -> {
            SpaceXEmptyState(
                message = emptyMessage,
                modifier = modifier,
                textColor = SpaceXColors.OnSurfaceVariant,
            )
        }

        else -> {
            // Memoize the LaunchData conversion to avoid recomposition
            val launchDataList =
                remember(launches) {
                    launches.map { launch ->
                        LaunchData(
                            id = launch.id,
                            name = launch.name,
                            date = launch.date,
                            time = launch.time,
                            rocketId = launch.rocketId,
                            launchpadId = launch.launchpadId,
                            patchImageUrl = launch.patchImageUrl,
                            windSpeed = launch.windSpeed,
                            cloudCover = launch.cloudCover,
                            rainfall = launch.rainfall,
                            countdown = launch.countdown,
                        )
                    }
                }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.CardMargin),
                    contentPadding = PaddingValues(SpaceXSpacing.HeaderPadding),
                ) {
                    itemsIndexed(
                        items = launchDataList,
                        key = { _, launchData -> launchData.id },
                        contentType = { _, _ -> "launch_card" },
                    ) { index, launchData ->
                        SpaceXLaunchCard(
                            launch = launchData,
                            onWatchClick = { onWatchClick(launches[index]) },
                            onClick = { onLaunchClick(launches[index]) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                if (isRefreshing) {
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.TopCenter)
                                .padding(SpaceXSpacing.Medium),
                    ) {
                        CircularProgressIndicator(
                            color = SpaceXColors.Primary,
                            modifier = Modifier.size(SpaceXSpacing.IconMedium),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpaceXLaunchListWithPagination(
    launches: List<LaunchListItem>,
    onLaunchClick: (LaunchListItem) -> Unit,
    onWatchClick: (LaunchListItem) -> Unit,
    paginationConfig: PaginationConfig,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(SpaceXSpacing.HeaderPadding),
    hasError: Boolean = false,
) {
    val listState = rememberLazyListState()

    val launchDataList =
        remember(launches) {
            launches.map { launch ->
                LaunchData(
                    id = launch.id,
                    name = launch.name,
                    date = launch.date,
                    time = launch.time,
                    rocketId = launch.rocketId,
                    launchpadId = launch.launchpadId,
                    patchImageUrl = launch.patchImageUrl,
                    windSpeed = launch.windSpeed,
                    cloudCover = launch.cloudCover,
                    rainfall = launch.rainfall,
                    countdown = launch.countdown,
                )
            }
        }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.CardMargin),
        contentPadding = contentPadding,
    ) {
        itemsIndexed(
            items = launchDataList,
            key = { _, launchData -> launchData.id },
            contentType = { _, _ -> "launch_card" },
        ) { index, launchData ->
            SpaceXLaunchCard(
                launch = launchData,
                onWatchClick = { onWatchClick(launches[index]) },
                onClick = { onLaunchClick(launches[index]) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (paginationConfig.isLoadingMore) {
            item {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(SpaceXSpacing.Medium),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = SpaceXColors.Primary,
                        modifier = Modifier.padding(SpaceXSpacing.Small),
                    )
                }
            }
        }

        if (paginationConfig.hasMoreItems && !paginationConfig.isLoadingMore && !hasError && launches.isNotEmpty()) {
            item {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(SpaceXSpacing.Medium),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Scroll to load more",
                        style = SpaceXTypography.Typography.bodySmall,
                        color = SpaceXColors.OnSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

    // Auto-load more when reaching the very end of the list
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index
        }.collect { lastVisibleIndex ->
            if (paginationConfig.hasMoreItems && !paginationConfig.isLoadingMore) {
                val totalItems = launches.size
                
                // Only trigger when reaching the very last item (index = totalItems - 1)
                if (lastVisibleIndex != null && lastVisibleIndex >= totalItems - 1) {
                    paginationConfig.onLoadMore()
                }
            }
        }
    }
}

@Composable
fun SpaceXLaunchListFiltered(
    launches: List<LaunchListItem>,
    onLaunchClick: (LaunchListItem) -> Unit,
    onWatchClick: (LaunchListItem) -> Unit,
    filter: (LaunchListItem) -> Boolean,
    modifier: Modifier = Modifier,
    emptyMessage: String = "No launches match your criteria",
) {
    // Memoize the filtered list to avoid recomputation on every recomposition
    val filteredLaunches =
        remember(launches, filter) {
            launches.filter(filter)
        }

    if (filteredLaunches.isEmpty()) {
        SpaceXEmptyState(
            message = emptyMessage,
            modifier = modifier,
        )
    } else {
        SpaceXLaunchList(
            launches = filteredLaunches,
            onLaunchClick = onLaunchClick,
            onWatchClick = onWatchClick,
            modifier = modifier,
        )
    }
}

@Composable
fun SpaceXLaunchListSection(
    title: String,
    launches: List<LaunchListItem>,
    onLaunchClick: (LaunchListItem) -> Unit,
    onWatchClick: (LaunchListItem) -> Unit,
    modifier: Modifier = Modifier,
    showTitle: Boolean = true,
    maxItems: Int? = null,
    emptyMessage: String = "No launches available",
) {
    // Memoize the display launches to avoid recomputation
    val displayLaunches =
        remember(launches, maxItems) {
            maxItems?.let { launches.take(it) } ?: launches
        }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
    ) {
        if (showTitle && displayLaunches.isNotEmpty()) {
            Text(
                text = title,
                style = SpaceXTypography.Typography.headlineSmall,
                color = SpaceXColors.OnSurface,
                modifier = Modifier.padding(horizontal = SpaceXSpacing.HeaderPadding),
            )
        }

        SpaceXLaunchList(
            launches = displayLaunches,
            onLaunchClick = onLaunchClick,
            onWatchClick = onWatchClick,
            contentPadding = PaddingValues(horizontal = SpaceXSpacing.HeaderPadding),
            emptyMessage = emptyMessage,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchListPreview() {
    SpaceXTheme {
        SpaceXLaunchList(
            launches =
                listOf(
                    LaunchListItem(
                        id = "1",
                        name = "Starlink Group 2-38",
                        date = "Oct/4/2025",
                        time = "10:00",
                        rocketId = "Falcon 9",
                        launchpadId = "SLC-40",
                        patchImageUrl = null,
                        windSpeed = "60%",
                        cloudCover = "35%",
                        rainfall = "0.0mm",
                        countdown = "89min",
                    ),
                    LaunchListItem(
                        id = "2",
                        name = "Falcon Heavy Demo",
                        date = "Nov/15/2025",
                        time = "14:30",
                        rocketId = "Falcon Heavy",
                        launchpadId = "KSC LC-39A",
                        patchImageUrl = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                        windSpeed = "45%",
                        cloudCover = "20%",
                        rainfall = "0.0mm",
                        countdown = "2h 15min",
                    ),
                ),
            onLaunchClick = { },
            onWatchClick = { },
            emptyMessage = "No launches available",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLoadingStatePreview() {
    SpaceXTheme {
        SpaceXLoadingState(
            message = "Loading launches...",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXEmptyStatePreview() {
    SpaceXTheme {
        SpaceXEmptyState(
            message = "No upcoming launches found",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchListSectionPreview() {
    SpaceXTheme {
        SpaceXLaunchListSection(
            title = "Upcoming Launches",
            launches =
                listOf(
                    LaunchListItem(
                        id = "1",
                        name = "Starlink Group 2-38",
                        date = "Oct/4/2025",
                        time = "10:00",
                        rocketId = "Falcon 9",
                        launchpadId = "SLC-40",
                        countdown = "89min",
                    ),
                ),
            onLaunchClick = { },
            onWatchClick = { },
            emptyMessage = "No launches available",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXErrorStatePreview() {
    SpaceXTheme {
        SpaceXErrorState(
            error =
                LaunchListError(
                    message = "Failed to load launches. Please check your connection and try again.",
                    retryAction = { },
                ),
            onRetry = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchListWithPaginationPreview() {
    SpaceXTheme {
        SpaceXLaunchListWithPagination(
            launches =
                listOf(
                    LaunchListItem(
                        id = "1",
                        name = "Starlink Group 2-38",
                        date = "Oct/4/2025",
                        time = "10:00",
                        rocketId = "Falcon 9",
                        launchpadId = "SLC-40",
                        countdown = "89min",
                    ),
                ),
            onLaunchClick = { },
            onWatchClick = { },
            paginationConfig =
                PaginationConfig(
                    isLoadingMore = true,
                    hasMoreItems = true,
                    onLoadMore = { },
                ),
            hasError = false,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchListFilteredPreview() {
    SpaceXTheme {
        SpaceXLaunchListFiltered(
            launches =
                listOf(
                    LaunchListItem(
                        id = "1",
                        name = "Starlink Group 2-38",
                        date = "Oct/4/2025",
                        time = "10:00",
                        rocketId = "Falcon 9",
                        launchpadId = "SLC-40",
                        countdown = "89min",
                    ),
                    LaunchListItem(
                        id = "2",
                        name = "Falcon Heavy Demo",
                        date = "Nov/15/2025",
                        time = "14:30",
                        rocketId = "Falcon Heavy",
                        launchpadId = "KSC LC-39A",
                        countdown = "2h 15min",
                    ),
                ),
            onLaunchClick = { },
            onWatchClick = { },
            filter = { it.name.contains("Starlink") },
            emptyMessage = "No Starlink launches found",
        )
    }
}
