package com.linkjf.spacex.launch.detail.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.linkjf.spacex.launch.designsystem.components.SpaceXErrorCard
import com.linkjf.spacex.launch.designsystem.components.SpaceXRateLimitCard
import com.linkjf.spacex.launch.designsystem.components.SpaceXLaunchImage
import com.linkjf.spacex.launch.designsystem.components.SpaceXPlayButton
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXIcons
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography
import com.linkjf.spacex.launch.mvi.EventEffect
import com.linkjf.spacex.launch.core.time.TimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun DetailScreen(
    launchData: LaunchDetailData? = null,
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onWatchWebcast: (String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Load launch details with the provided data
    LaunchedEffect(launchData) {
        if (launchData != null) {
            viewModel.loadLaunchDetails(launchData)
        }
    }

    DetailScreenContent(
        state = state,
        events = viewModel.event,
        onAction = viewModel::reduce,
        onBackClick = onBackClick,
        onWatchWebcast = onWatchWebcast,
    )
}

@Composable
private fun DetailScreenContent(
    state: DetailState,
    events: Flow<DetailEvent>,
    onAction: (DetailAction) -> Unit,
    onBackClick: () -> Unit,
    onWatchWebcast: (String) -> Unit,
) {
    EventEffect(flow = events) { event ->
        when (event) {
            DetailEvent.NavigateBack -> onBackClick()
            DetailEvent.ShowMenu -> {
                // TODO: Implement menu
            }
            is DetailEvent.NavigateToWebcast -> onWatchWebcast(event.url)
            is DetailEvent.ShowError -> {
                // Error is handled in state
            }
            is DetailEvent.ShowRateLimitError -> {
                // Rate limit error is handled in state
            }
        }
    }

    Scaffold(
        containerColor = SpaceXColors.Background,
    ) { paddingValues ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = SpaceXColors.Primary,
                    )
                }

                state.errorMessage != null -> {
                    Column {
                        state.rateLimitError?.let { rateLimitError ->
                            SpaceXRateLimitCard(
                                message =
                                    "Rate limit exceeded. Please wait ${TimeUtils.formatDuration(rateLimitError.retryAfterSeconds ?: 0)}.",
                                retryAfterSeconds = rateLimitError.retryAfterSeconds,
                                onDismiss = { onAction(DetailAction.DismissRateLimitError) },
                                modifier = Modifier.padding(SpaceXSpacing.Medium),
                            )
                        }
                        SpaceXErrorCard(
                            message = state.errorMessage,
                            onDismiss = { onAction(DetailAction.DismissError) },
                            modifier =
                                Modifier
                                    .padding(SpaceXSpacing.Medium),
                        )
                    }
                }

                else -> {
                    Column {
                        state.rateLimitError?.let { rateLimitError ->
                            SpaceXRateLimitCard(
                                message =
                                    "Rate limit exceeded. Please wait ${TimeUtils.formatDuration(rateLimitError.retryAfterSeconds ?: 0)}.",
                                retryAfterSeconds = rateLimitError.retryAfterSeconds,
                                onDismiss = { onAction(DetailAction.DismissRateLimitError) },
                                modifier =
                                    Modifier
                                        .padding(SpaceXSpacing.Medium)
                                        .align(Alignment.CenterHorizontally),
                            )
                        }
                        DetailContent(
                            state = state,
                            onAction = onAction,
                        )
                    }
                }
            }

            // Header overlay
            DetailHeader(
                onBackClick = { onAction(DetailAction.OnBackClicked) },
                onMenuClick = { onAction(DetailAction.OnMenuClicked) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart),
            )
        }
    }
}

@Composable
private fun DetailContent(
    state: DetailState,
    onAction: (DetailAction) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp),
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // Mission Patch Image
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpaceXSpacing.Large),
            contentAlignment = Alignment.Center,
        ) {
            SpaceXLaunchImage(
                imageUrl = state.patchImageUrl,
                contentDescription = "${state.launchName} patch",
                modifier = Modifier.size(166.dp),
                imageSize = 166.dp,
            )
        }

        Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

        // Launch Name
        Text(
            text = state.launchName,
            style = SpaceXTypography.Typography.headlineLarge,
            color = SpaceXColors.OnSurface,
            fontWeight = FontWeight.Bold,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpaceXSpacing.HeaderPadding),
        )

        Spacer(modifier = Modifier.height(SpaceXSpacing.Small))

        // Launch Code
        Text(
            text = state.launchCode,
            style = SpaceXTypography.Typography.bodyLarge,
            color = SpaceXColors.OnSurface,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpaceXSpacing.HeaderPadding),
        )

        Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

        // Status Badge
        if (state.isLive) {
            Text(
                text = "On live",
                style = SpaceXTypography.Typography.headlineMedium,
                color = Color(0xFFFF9393),
                fontWeight = FontWeight.Medium,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SpaceXSpacing.HeaderPadding),
            )
            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))
        }

        // Weather Card
        WeatherCard(
            weatherData = state.weatherData,
            location = state.location,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SpaceXSpacing.HeaderPadding),
        )

        Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

        // Maps Placeholder
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(282.dp)
                    .padding(horizontal = SpaceXSpacing.HeaderPadding),
            colors =
                CardDefaults.cardColors(
                    containerColor = SpaceXColors.CardBackground,
                ),
            shape = RoundedCornerShape(SpaceXSpacing.BorderRadiusMedium),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "MAPS",
                    style = SpaceXTypography.Typography.titleMedium,
                    color = SpaceXColors.OnSurface,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }

    // Watch Button (Fixed at bottom)
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(bottom = SpaceXSpacing.Medium),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Card(
            onClick = { onAction(DetailAction.OnWatchClicked) },
            modifier =
                Modifier
                    .padding(horizontal = SpaceXSpacing.Large * 2),
            colors =
                CardDefaults.cardColors(
                    containerColor = SpaceXColors.CardBackground,
                ),
            shape = RoundedCornerShape(SpaceXSpacing.BorderRadiusLarge),
        ) {
            Row(
                modifier = Modifier.padding(SpaceXSpacing.Medium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
            ) {
                SpaceXPlayButton(
                    onClick = { onAction(DetailAction.OnWatchClicked) },
                    iconColor = SpaceXColors.OnSurface,
                    contentDescription = "Watch launch",
                )
                Text(
                    text = "Watch",
                    style = SpaceXTypography.Typography.bodyLarge,
                    color = SpaceXColors.OnSurface,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun DetailHeader(
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(SpaceXSpacing.HeaderPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = SpaceXIcons.ArrowBack,
                contentDescription = "Back",
                tint = SpaceXColors.OnSurface,
                modifier = Modifier.size(SpaceXSpacing.IconMedium),
            )
        }

        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = SpaceXIcons.MoreVert,
                contentDescription = "Menu",
                tint = SpaceXColors.OnSurface,
                modifier = Modifier.size(SpaceXSpacing.IconMedium),
            )
        }
    }
}

@Composable
private fun WeatherCard(
    weatherData: WeatherDetailData,
    location: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors =
            CardDefaults.cardColors(
                containerColor = SpaceXColors.CardBackground,
            ),
        shape = RoundedCornerShape(SpaceXSpacing.BorderRadiusMedium),
    ) {
        Column(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
        ) {
            WeatherRow(label = "Wind", value = weatherData.windSpeed)
            WeatherRow(label = "Gust", value = weatherData.windGust)
            WeatherRow(label = "Cloud cover", value = weatherData.cloudCover)
            WeatherRow(label = "Presipitation prob", value = weatherData.precipitationProbability)
            WeatherRow(label = "Visibility", value = weatherData.visibility)
            WeatherRow(label = "Ubication", value = location)
        }
    }
}

@Composable
private fun WeatherRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = SpaceXSpacing.Small),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = SpaceXTypography.Typography.bodyLarge,
            color = SpaceXColors.OnSurface,
            fontWeight = FontWeight.Medium,
        )

        Text(
            text = value,
            style = SpaceXTypography.Typography.bodyLarge,
            color = SpaceXColors.OnSurface,
            fontWeight = FontWeight.Light,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun DetailScreenPreview() {
    SpaceXTheme {
        DetailScreenContent(
            state =
                DetailState(
                    isLoading = false,
                    launchId = "launch_1",
                    launchName = "Starlink Group 2-38",
                    launchCode = "EU89 - LCA827",
                    dateUtc = "2025-10-04T10:00:00Z",
                    patchImageUrl = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                    status = LaunchDetailStatus.SCHEDULED,
                    isLive = false,
                    rocketName = "Falcon 9",
                    launchpadName = "Space Launch Complex 40",
                    location = "Cape Canaveral Florida",
                    details = "A batch of Starlink satellites for global internet coverage.",
                    webcastUrl = "https://youtube.com/watch?v=example",
                    weatherData =
                        WeatherDetailData(
                            windSpeed = "55 m/h",
                            windGust = "88mix",
                            cloudCover = "24",
                            precipitationProbability = "52ik",
                                    visibility = "1.3km",
                                ),
                                errorMessage = null,
                                rateLimitError = null,
                            ),
            events = flowOf(),
            onAction = { },
            onBackClick = { },
            onWatchWebcast = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun DetailScreenLivePreview() {
    SpaceXTheme {
        DetailScreenContent(
            state =
                DetailState(
                    isLoading = false,
                    launchId = "launch_2",
                    launchName = "Falcon Heavy Demo",
                    launchCode = "FH-001 - LC-39A",
                    dateUtc = "2025-10-07T14:30:00Z",
                    patchImageUrl = null,
                    status = LaunchDetailStatus.LIVE,
                    isLive = true,
                    rocketName = "Falcon Heavy",
                    launchpadName = "Kennedy Space Center LC-39A",
                    location = "Cape Canaveral Florida",
                    details = "First test flight of the Falcon Heavy rocket.",
                    webcastUrl = "https://youtube.com/watch?v=example",
                    weatherData =
                        WeatherDetailData(
                            windSpeed = "45 m/h",
                            windGust = "75mix",
                            cloudCover = "15",
                            precipitationProbability = "10ik",
                            visibility = "8.5km",
                        ),
                    errorMessage = null,
                    rateLimitError = null,
                ),
            events = flowOf(),
            onAction = { },
            onBackClick = { },
            onWatchWebcast = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun DetailScreenLoadingPreview() {
    SpaceXTheme {
        DetailScreenContent(
            state =
                DetailState(
                    isLoading = true,
                ),
            events = flowOf(),
            onAction = { },
            onBackClick = { },
            onWatchWebcast = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun DetailScreenErrorPreview() {
    SpaceXTheme {
        DetailScreenContent(
            state =
                DetailState(
                    isLoading = false,
                    errorMessage = "Failed to load launch details. Please try again.",
                    rateLimitError = null,
                ),
            events = flowOf(),
            onAction = { },
            onBackClick = { },
            onWatchWebcast = { },
        )
    }
}

