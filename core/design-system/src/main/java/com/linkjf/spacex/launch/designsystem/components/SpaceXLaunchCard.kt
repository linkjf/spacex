package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXIcons
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography

data class LaunchData(
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
)

@Composable
fun SpaceXLaunchCard(
    launch: LaunchData,
    onWatchClick: () -> Unit,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    backgroundColor: Color = SpaceXColors.CardBackground,
    textColor: Color = SpaceXColors.OnSurface,
    dividerColor: Color = SpaceXColors.Divider,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick() },
        colors =
            CardDefaults.cardColors(
                containerColor = backgroundColor,
            ),
        shape = RoundedCornerShape(SpaceXSpacing.BorderRadiusMedium),
    ) {
        Column(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
                ) {
                    Text(
                        text = launch.name,
                        style = SpaceXTypography.Typography.headlineLarge,
                        color = textColor,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
                        ) {
                            Text(
                                text = launch.date,
                                style = SpaceXTypography.Typography.bodyLarge,
                                color = textColor,
                            )
                            Text(
                                text = launch.rocketId,
                                style = SpaceXTypography.Typography.bodyLarge,
                                color = textColor,
                            )
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
                        ) {
                            Text(
                                text = launch.time,
                                style = SpaceXTypography.Typography.bodyLarge,
                                color = textColor,
                            )

                            Text(
                                text = launch.launchpadId,
                                style = SpaceXTypography.Typography.bodyLarge,
                                color = textColor,
                            )
                        }
                    }
                }

                SpaceXLaunchImage(
                    imageUrl = launch.patchImageUrl,
                    contentDescription = "${launch.name} patch",
                )
            }

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            HorizontalDivider(
                color = dividerColor,
                thickness = SpaceXSpacing.DividerThicknessDefault.dp,
            )

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SpaceXWeatherMetricsRow(
                    windSpeed = launch.windSpeed,
                    cloudCover = launch.cloudCover,
                    rainfall = launch.rainfall,
                    textColor = textColor,
                )

                Text(
                    text = launch.countdown,
                    style = SpaceXTypography.countdownTimer,
                    color = textColor,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }

            Spacer(modifier = Modifier.height(SpaceXSpacing.Small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SpaceXWeatherMetric(
                    value = launch.rainfall,
                    label = "Rain:",
                    icon = SpaceXIcons.Rain,
                    textColor = textColor,
                    iconColor = textColor,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
                ) {
                    SpaceXPlayButton(
                        onClick = onWatchClick,
                        iconColor = textColor,
                    )
                    Text(
                        text = "Watch",
                        style = SpaceXTypography.Typography.bodyMedium,
                        color = textColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchCardPreview() {
    SpaceXTheme {
        SpaceXLaunchCard(
            launch =
                LaunchData(
                    id = "launch_1",
                    name = "Starlink Group 2-38",
                    date = "Oct/4/2025",
                    time = "10:00",
                    rocketId = "Falcon 4",
                    launchpadId = "LCS-421",
                    patchImageUrl = null,
                    windSpeed = "60%",
                    cloudCover = "35%",
                    rainfall = "0.0mm",
                    countdown = "89min",
                ),
            onWatchClick = { },
            onClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchCardWithImagePreview() {
    SpaceXTheme {
        SpaceXLaunchCard(
            launch =
                LaunchData(
                    id = "launch_2",
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
            onWatchClick = { },
            onClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchCardLongNamePreview() {
    SpaceXTheme {
        SpaceXLaunchCard(
            launch =
                LaunchData(
                    id = "launch_3",
                    name = "Starlink Group 6-45 Mission",
                    date = "Dec/20/2025",
                    time = "08:45",
                    rocketId = "Falcon 9",
                    launchpadId = "SLC-40",
                    patchImageUrl = null,
                    windSpeed = "80%",
                    cloudCover = "60%",
                    rainfall = "1.2mm",
                    countdown = "5d 12h",
                ),
            onWatchClick = { },
            onClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
        )
    }
}
