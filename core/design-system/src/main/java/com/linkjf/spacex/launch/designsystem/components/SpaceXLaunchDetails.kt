package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.designsystem.R
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography

data class LaunchDetailsData(
    val id: String,
    val name: String,
    val dateUtc: String,
    val rocketName: String,
    val launchpadName: String,
    val missionType: String,
    val details: String?,
    val patchImageUrl: String?,
    val webcastUrl: String?,
    val status: LaunchStatus,
)

enum class LaunchStatus {
    SCHEDULED,
    LAUNCHED,
    FAILED,
    CANCELLED,
}

@Composable
fun SpaceXLaunchDetails(
    launch: LaunchDetailsData,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SpaceXColors.CardBackground,
    textColor: Color = SpaceXColors.OnSurface,
    dividerColor: Color = SpaceXColors.Divider,
    patchContentDescription: String =
        stringResource(
            R.string.launch_details_patch_description,
            launch.name,
        ),
    rocketLabel: String = stringResource(R.string.launch_details_rocket),
    launchpadLabel: String = stringResource(R.string.launch_details_launchpad),
    missionTypeLabel: String = stringResource(R.string.launch_details_mission_type),
    detailsLabel: String = stringResource(R.string.launch_details_details),
    scheduledText: String = stringResource(R.string.launch_status_scheduled),
    launchedText: String = stringResource(R.string.launch_status_launched),
    failedText: String = stringResource(R.string.launch_status_failed),
    cancelledText: String = stringResource(R.string.launch_status_cancelled),
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                        fontWeight = FontWeight.Bold,
                    )

                    Text(
                        text = launch.dateUtc,
                        style = SpaceXTypography.Typography.titleLarge,
                        color = textColor.copy(alpha = SpaceXSpacing.LaunchDetailsDateAlpha),
                    )

                    SpaceXLaunchStatus(
                        status = launch.status,
                        textColor = textColor,
                        scheduledText = scheduledText,
                        launchedText = launchedText,
                        failedText = failedText,
                        cancelledText = cancelledText,
                    )
                }

                SpaceXLaunchImage(
                    imageUrl = launch.patchImageUrl,
                    contentDescription = patchContentDescription,
                    modifier = Modifier.size(SpaceXSpacing.LaunchDetailsImageSize),
                )
            }

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            HorizontalDivider(
                color = dividerColor,
                thickness = SpaceXSpacing.DividerThickness,
            )

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            Column(
                verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small2),
            ) {
                SpaceXDetailRow(
                    label = rocketLabel,
                    value = launch.rocketName,
                    textColor = textColor,
                )

                SpaceXDetailRow(
                    label = launchpadLabel,
                    value = launch.launchpadName,
                    textColor = textColor,
                )

                SpaceXDetailRow(
                    label = missionTypeLabel,
                    value = launch.missionType,
                    textColor = textColor,
                )

                launch.details?.let { details ->
                    SpaceXDetailRow(
                        label = detailsLabel,
                        value = details,
                        textColor = textColor,
                        isMultiline = true,
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceXDetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    isMultiline: Boolean = false,
) {
    if (isMultiline) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Micro),
        ) {
            Text(
                text = label,
                style = SpaceXTypography.Typography.labelLarge,
                color = SpaceXColors.OnSurfaceVariant,
                fontWeight = FontWeight.Medium,
            )
            Text(
                text = value,
                style = SpaceXTypography.Typography.bodyLarge,
                color = textColor,
            )
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = SpaceXTypography.Typography.labelLarge,
                color = SpaceXColors.OnSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )

            Text(
                text = value,
                style = SpaceXTypography.Typography.bodyLarge,
                color = textColor,
                modifier = Modifier.weight(2f),
            )
        }
    }
}

@Composable
fun SpaceXLaunchStatus(
    status: LaunchStatus,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    scheduledText: String = stringResource(R.string.launch_status_scheduled),
    launchedText: String = stringResource(R.string.launch_status_launched),
    failedText: String = stringResource(R.string.launch_status_failed),
    cancelledText: String = stringResource(R.string.launch_status_cancelled),
) {
    val (statusText, statusColor) =
        when (status) {
            LaunchStatus.SCHEDULED -> scheduledText to SpaceXColors.Primary
            LaunchStatus.LAUNCHED -> launchedText to SpaceXColors.Success
            LaunchStatus.FAILED -> failedText to SpaceXColors.Error
            LaunchStatus.CANCELLED -> cancelledText to SpaceXColors.OnSurfaceVariant
        }

    Text(
        text = statusText,
        style = SpaceXTypography.Typography.labelLarge,
        color = statusColor,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchDetailsPreview() {
    SpaceXTheme {
        SpaceXLaunchDetails(
            launch =
                LaunchDetailsData(
                    id = "launch_1",
                    name = "Starlink Group 2-38",
                    dateUtc = "Oct 4, 2025 at 10:00 UTC",
                    rocketName = "Falcon 9",
                    launchpadName = "Space Launch Complex 40",
                    missionType = "Commercial",
                    details =
                        "A batch of Starlink satellites for global internet coverage." +
                            " This mission will deploy satellites into low Earth orbit to expand SpaceX's satellite constellation.",
                    patchImageUrl = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                    webcastUrl = "https://youtube.com/watch?v=example",
                    status = LaunchStatus.SCHEDULED,
                ),
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
            patchContentDescription = "Starlink Group 2-38 mission patch",
            rocketLabel = "Rocket",
            launchpadLabel = "Launchpad",
            missionTypeLabel = "Mission Type",
            detailsLabel = "Details",
            scheduledText = "Scheduled",
            launchedText = "Launched",
            failedText = "Failed",
            cancelledText = "Cancelled",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchDetailsFailedPreview() {
    SpaceXTheme {
        SpaceXLaunchDetails(
            launch =
                LaunchDetailsData(
                    id = "launch_2",
                    name = "Falcon Heavy Demo",
                    dateUtc = "Feb 6, 2018 at 20:45 UTC",
                    rocketName = "Falcon Heavy",
                    launchpadName = "Kennedy Space Center LC-39A",
                    missionType = "Test Flight",
                    details = "First test flight of the Falcon Heavy rocket carrying a Tesla Roadster as payload.",
                    patchImageUrl = null,
                    webcastUrl = null,
                    status = LaunchStatus.LAUNCHED,
                ),
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
            patchContentDescription = "Falcon Heavy Demo mission patch",
            rocketLabel = "Rocket",
            launchpadLabel = "Launchpad",
            missionTypeLabel = "Mission Type",
            detailsLabel = "Details",
            scheduledText = "Scheduled",
            launchedText = "Launched",
            failedText = "Failed",
            cancelledText = "Cancelled",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXDetailRowPreview() {
    SpaceXTheme {
        Column(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small2),
        ) {
            SpaceXDetailRow(
                label = "Rocket",
                value = "Falcon 9",
                textColor = SpaceXColors.OnSurface,
            )

            SpaceXDetailRow(
                label = "Launchpad",
                value = "Space Launch Complex 40",
                textColor = SpaceXColors.OnSurface,
            )

            SpaceXDetailRow(
                label = "Mission Type",
                value = "Commercial",
                textColor = SpaceXColors.OnSurface,
            )

            SpaceXDetailRow(
                label = "Details",
                value =
                    "A batch of Starlink satellites for global internet coverage." +
                        " This mission will deploy satellites into low Earth orbit.",
                textColor = SpaceXColors.OnSurface,
                isMultiline = true,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchStatusPreview() {
    SpaceXTheme {
        Column(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small2),
        ) {
            SpaceXLaunchStatus(
                status = LaunchStatus.SCHEDULED,
                textColor = SpaceXColors.OnSurface,
                scheduledText = "Scheduled",
                launchedText = "Launched",
                failedText = "Failed",
                cancelledText = "Cancelled",
            )

            SpaceXLaunchStatus(
                status = LaunchStatus.LAUNCHED,
                textColor = SpaceXColors.OnSurface,
                scheduledText = "Scheduled",
                launchedText = "Launched",
                failedText = "Failed",
                cancelledText = "Cancelled",
            )

            SpaceXLaunchStatus(
                status = LaunchStatus.FAILED,
                textColor = SpaceXColors.OnSurface,
                scheduledText = "Scheduled",
                launchedText = "Launched",
                failedText = "Failed",
                cancelledText = "Cancelled",
            )

            SpaceXLaunchStatus(
                status = LaunchStatus.CANCELLED,
                textColor = SpaceXColors.OnSurface,
                scheduledText = "Scheduled",
                launchedText = "Launched",
                failedText = "Failed",
                cancelledText = "Cancelled",
            )
        }
    }
}
