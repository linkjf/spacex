package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.designsystem.R
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography

data class InteractiveLaunchData(
    val id: String,
    val name: String,
    val date: String,
    val status: String,
    val imageUrl: String? = null,
)

@Composable
fun SpaceXInteractiveLaunchCardWithResources(
    launch: InteractiveLaunchData,
    onClick: () -> Unit,
    onWatchClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SpaceXColors.CardBackground,
    textColor: Color = SpaceXColors.OnSurface,
    dividerColor: Color = SpaceXColors.Divider,
    enabled: Boolean = true,
    isPressed: Boolean = false,
) {
    SpaceXInteractiveLaunchCard(
        launch = launch,
        onClick = onClick,
        onWatchClick = onWatchClick,
        modifier = modifier,
        backgroundColor = backgroundColor,
        textColor = textColor,
        dividerColor = dividerColor,
        enabled = enabled,
        isPressed = isPressed,
        watchButtonContentDescription = stringResource(R.string.interactive_card_watch_default),
        onClickLabel = stringResource(R.string.interactive_card_view_details, launch.name),
        patchContentDescription =
            stringResource(
                R.string.interactive_card_patch_description,
                launch.name,
            ),
    )
}

@Composable
fun SpaceXInteractiveLaunchCard(
    launch: InteractiveLaunchData,
    onClick: () -> Unit,
    onWatchClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SpaceXColors.CardBackground,
    textColor: Color = SpaceXColors.OnSurface,
    dividerColor: Color = SpaceXColors.Divider,
    enabled: Boolean = true,
    isPressed: Boolean = false,
    watchButtonContentDescription: String = "Watch launch",
    onClickLabel: String = "View launch details",
    patchContentDescription: String = "Launch patch",
) {
    val scale by animateFloatAsState(
        targetValue = if (isPressed) SpaceXSpacing.InteractiveCardPressedScale else 1f,
        animationSpec = tween(durationMillis = SpaceXSpacing.InteractiveCardAnimationDuration),
        label = "scale",
    )

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = enabled,
                    onClickLabel = onClickLabel,
                    role = Role.Button,
                    onClick = onClick,
                ),
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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = launch.name,
                        style = SpaceXTypography.Typography.titleMedium,
                        color = textColor,
                    )
                    Text(
                        text = launch.date,
                        style = SpaceXTypography.Typography.bodyMedium,
                        color = SpaceXColors.OnSurfaceVariant,
                    )
                }

                SpaceXPlayButton(
                    onClick = onWatchClick,
                    iconColor = SpaceXColors.OnSurface,
                    enabled = enabled,
                    contentDescription = watchButtonContentDescription,
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = SpaceXSpacing.Small2),
                thickness = SpaceXSpacing.DividerThickness,
                color = dividerColor,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = launch.status,
                    style = SpaceXTypography.Typography.labelMedium,
                    color = SpaceXColors.OnSurfaceVariant,
                )

                SpaceXLaunchImage(
                    imageUrl = launch.imageUrl,
                    contentDescription = patchContentDescription,
                    modifier = Modifier.size(SpaceXSpacing.LaunchImageSize),
                )
            }
        }
    }
}

@Composable
fun SpaceXInteractiveCardGrid(
    launches: List<InteractiveLaunchData>,
    onLaunchClick: (InteractiveLaunchData) -> Unit,
    onWatchClick: (InteractiveLaunchData) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    watchButtonContentDescription: String = "Watch launch",
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.CardMargin),
    ) {
        launches.forEach { launch ->
            SpaceXInteractiveLaunchCard(
                launch = launch,
                onClick = { onLaunchClick(launch) },
                onWatchClick = { onWatchClick(launch) },
                enabled = enabled,
                watchButtonContentDescription = watchButtonContentDescription,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXInteractiveLaunchCardPreview() {
    SpaceXTheme {
        SpaceXInteractiveLaunchCard(
            launch =
                InteractiveLaunchData(
                    id = "1",
                    name = "FalconSat",
                    date = "Mar 24, 2006",
                    status = "Failed",
                    imageUrl = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                ),
            onClick = { },
            onWatchClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            watchButtonContentDescription = "Watch FalconSat launch",
            onClickLabel = "View launch details for FalconSat",
            patchContentDescription = "FalconSat patch",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXInteractiveLaunchCardPressedPreview() {
    SpaceXTheme {
        SpaceXInteractiveLaunchCard(
            launch =
                InteractiveLaunchData(
                    id = "2",
                    name = "DemoSat",
                    date = "Mar 21, 2007",
                    status = "Success",
                    imageUrl = null,
                ),
            onClick = { },
            onWatchClick = { },
            isPressed = true,
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            watchButtonContentDescription = "Watch DemoSat launch",
            onClickLabel = "View launch details for DemoSat",
            patchContentDescription = "DemoSat patch",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXInteractiveCardGridPreview() {
    SpaceXTheme {
        SpaceXInteractiveCardGrid(
            launches =
                listOf(
                    InteractiveLaunchData(
                        id = "1",
                        name = "FalconSat",
                        date = "Mar 24, 2006",
                        status = "Failed",
                        imageUrl = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                    ),
                    InteractiveLaunchData(
                        id = "2",
                        name = "DemoSat",
                        date = "Mar 21, 2007",
                        status = "Success",
                        imageUrl = null,
                    ),
                    InteractiveLaunchData(
                        id = "3",
                        name = "Trailblazer",
                        date = "Aug 3, 2008",
                        status = "Success",
                        imageUrl = "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png",
                    ),
                ),
            onLaunchClick = { },
            onWatchClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            watchButtonContentDescription = "Watch launch",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXInteractiveLaunchCardDisabledPreview() {
    SpaceXTheme {
        SpaceXInteractiveLaunchCard(
            launch =
                InteractiveLaunchData(
                    id = "4",
                    name = "RatSat",
                    date = "Sep 28, 2008",
                    status = "Success",
                    imageUrl = null,
                ),
            onClick = { },
            onWatchClick = { },
            enabled = false,
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            watchButtonContentDescription = "Watch RatSat launch",
            onClickLabel = "View launch details for RatSat",
            patchContentDescription = "RatSat patch",
        )
    }
}
