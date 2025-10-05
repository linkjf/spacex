package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.designsystem.R
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography
import com.linkjf.spacex.launch.tools.time.CountdownStatus
import com.linkjf.spacex.launch.tools.time.TimeRemaining
import com.linkjf.spacex.launch.tools.time.calculateLaunchProgress
import com.linkjf.spacex.launch.tools.time.calculateTimeRemaining
import com.linkjf.spacex.launch.tools.time.createFutureInstant
import com.linkjf.spacex.launch.tools.time.createPastInstant
import com.linkjf.spacex.launch.tools.time.formatToUtcString
import com.linkjf.spacex.launch.tools.time.getCountdownStatus
import java.time.Instant

data class CountdownData(
    val launchInstant: Instant,
    val launchName: String,
    val isLive: Boolean = false,
)

@Composable
fun SpaceXCountdownTimer(
    countdown: CountdownData,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SpaceXColors.CardBackground,
    textColor: Color = SpaceXColors.OnSurface,
    dividerColor: Color = SpaceXColors.Divider,
    primaryColor: Color = SpaceXColors.Primary,
    successColor: Color = SpaceXColors.Success,
    errorColor: Color = SpaceXColors.Error,
    countdownToLaunchText: String = stringResource(R.string.countdown_to_launch),
    countingText: String = stringResource(R.string.status_counting),
    liveText: String = stringResource(R.string.status_live),
    launchedText: String = stringResource(R.string.status_launched),
    overdueText: String = stringResource(R.string.status_overdue),
    daysText: String = stringResource(R.string.time_unit_days),
    hoursText: String = stringResource(R.string.time_unit_hours),
    minutesText: String = stringResource(R.string.time_unit_minutes),
    secondsText: String = stringResource(R.string.time_unit_seconds),
) {
    var currentTime by remember { mutableStateOf(Instant.now()) }

    val timeRemaining = calculateTimeRemaining(countdown.launchInstant, currentTime)
    val progress = calculateLaunchProgress(countdown.launchInstant, currentTime)
    val status = getCountdownStatus(timeRemaining, countdown.isLive)

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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = countdownToLaunchText,
                    style = SpaceXTypography.Typography.titleLarge,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )

                SpaceXCountdownStatus(
                    status = status,
                    primaryColor = primaryColor,
                    successColor = successColor,
                    errorColor = errorColor,
                    countingText = countingText,
                    liveText = liveText,
                    launchedText = launchedText,
                    overdueText = overdueText,
                )
            }

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            HorizontalDivider(
                color = dividerColor,
                thickness = SpaceXSpacing.DividerThickness,
            )

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            Text(
                text = countdown.launchName,
                style = SpaceXTypography.Typography.headlineSmall,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(SpaceXSpacing.Small))

            Text(
                text = countdown.launchInstant.formatToUtcString(),
                style = SpaceXTypography.Typography.bodyLarge,
                color = SpaceXColors.OnSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            when (status) {
                CountdownStatus.COUNTING -> {
                    SpaceXCountdownDisplay(
                        timeRemaining = timeRemaining,
                        textColor = textColor,
                        primaryColor = primaryColor,
                        daysText = daysText,
                        hoursText = hoursText,
                        minutesText = minutesText,
                        secondsText = secondsText,
                    )

                    Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

                    SpaceXCountdownProgress(
                        progress = progress,
                        primaryColor = primaryColor,
                    )
                }

                CountdownStatus.LIVE -> {
                    SpaceXLiveIndicator(
                        textColor = textColor,
                        successColor = successColor,
                    )
                }

                CountdownStatus.LAUNCHED -> {
                    SpaceXLaunchedIndicator(
                        textColor = textColor,
                        successColor = successColor,
                    )
                }

                CountdownStatus.OVERDUE -> {
                    SpaceXOverdueIndicator(
                        textColor = textColor,
                        errorColor = errorColor,
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceXCountdownDisplay(
    timeRemaining: TimeRemaining,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    primaryColor: Color = SpaceXColors.Primary,
    daysText: String = stringResource(R.string.time_unit_days),
    hoursText: String = stringResource(R.string.time_unit_hours),
    minutesText: String = stringResource(R.string.time_unit_minutes),
    secondsText: String = stringResource(R.string.time_unit_seconds),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        SpaceXTimeUnit(
            value = timeRemaining.days,
            unit = daysText,
            textColor = textColor,
            primaryColor = primaryColor,
        )

        SpaceXTimeUnit(
            value = timeRemaining.hours,
            unit = hoursText,
            textColor = textColor,
            primaryColor = primaryColor,
        )

        SpaceXTimeUnit(
            value = timeRemaining.minutes,
            unit = minutesText,
            textColor = textColor,
            primaryColor = primaryColor,
        )

        SpaceXTimeUnit(
            value = timeRemaining.seconds,
            unit = secondsText,
            textColor = textColor,
            primaryColor = primaryColor,
        )
    }
}

@Composable
fun SpaceXTimeUnit(
    value: Int,
    unit: String,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    primaryColor: Color = SpaceXColors.Primary,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
    ) {
        Box(
            modifier =
                Modifier
                    .size(SpaceXSpacing.TimeUnitBox)
                    .clip(RoundedCornerShape(SpaceXSpacing.BorderRadiusSmall))
                    .padding(SpaceXSpacing.Small),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = value.toString().padStart(2, '0'),
                style = SpaceXTypography.Typography.headlineLarge,
                color = primaryColor,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = unit,
            style = SpaceXTypography.Typography.labelMedium,
            color = textColor,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
fun SpaceXCountdownProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    primaryColor: Color = SpaceXColors.Primary,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
    ) {
        Text(
            text = "Launch Progress",
            style = SpaceXTypography.Typography.labelLarge,
            color = SpaceXColors.OnSurfaceVariant,
            fontWeight = FontWeight.Medium,
        )

        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.size(SpaceXSpacing.ProgressIndicatorSize),
            color = primaryColor,
            strokeWidth = SpaceXSpacing.ProgressIndicatorStroke,
            trackColor = SpaceXColors.SurfaceVariant,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )

        Text(
            text = "${(progress * 100).toInt()}%",
            style = SpaceXTypography.Typography.titleMedium,
            color = primaryColor,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun SpaceXCountdownStatus(
    status: CountdownStatus,
    modifier: Modifier = Modifier,
    primaryColor: Color = SpaceXColors.Primary,
    successColor: Color = SpaceXColors.Success,
    errorColor: Color = SpaceXColors.Error,
    countingText: String = stringResource(R.string.status_counting),
    liveText: String = stringResource(R.string.status_live),
    launchedText: String = stringResource(R.string.status_launched),
    overdueText: String = stringResource(R.string.status_overdue),
) {
    val (text, color) =
        when (status) {
            CountdownStatus.COUNTING -> countingText to primaryColor
            CountdownStatus.LIVE -> liveText to successColor
            CountdownStatus.LAUNCHED -> launchedText to successColor
            CountdownStatus.OVERDUE -> overdueText to errorColor
        }

    Text(
        text = text,
        style = SpaceXTypography.Typography.labelLarge,
        color = color,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
    )
}

@Composable
fun SpaceXStatusIndicator(
    primaryText: String,
    secondaryText: String,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    primaryTextColor: Color = SpaceXColors.Success,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
        ) {
            Text(
                text = primaryText,
                style = SpaceXTypography.Typography.headlineLarge,
                color = primaryTextColor,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = secondaryText,
                style = SpaceXTypography.Typography.titleMedium,
                color = textColor,
            )
        }
    }
}

@Composable
fun SpaceXLiveIndicator(
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    successColor: Color = SpaceXColors.Success,
) {
    SpaceXStatusIndicator(
        primaryText = stringResource(R.string.live_indicator),
        secondaryText = stringResource(R.string.launch_in_progress),
        modifier = modifier,
        textColor = textColor,
        primaryTextColor = successColor,
    )
}

@Composable
fun SpaceXLaunchedIndicator(
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    successColor: Color = SpaceXColors.Success,
) {
    SpaceXStatusIndicator(
        primaryText = stringResource(R.string.launched_indicator),
        secondaryText = stringResource(R.string.mission_successfully_launched),
        modifier = modifier,
        textColor = textColor,
        primaryTextColor = successColor,
    )
}

@Composable
fun SpaceXOverdueIndicator(
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    errorColor: Color = SpaceXColors.Error,
) {
    SpaceXStatusIndicator(
        primaryText = stringResource(R.string.overdue_indicator),
        secondaryText = stringResource(R.string.launch_time_has_passed),
        modifier = modifier,
        textColor = textColor,
        primaryTextColor = errorColor,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXCountdownTimerPreview() {
    SpaceXTheme {
        SpaceXCountdownTimer(
            countdown =
                CountdownData(
                    launchInstant = createFutureInstant(days = 2, hours = 5, minutes = 30),
                    launchName = "Starlink Group 2-38",
                    isLive = false,
                ),
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
            countdownToLaunchText = "Countdown to Launch",
            countingText = "Counting",
            liveText = "LIVE",
            launchedText = "Launched",
            overdueText = "Overdue",
            daysText = "Days",
            hoursText = "Hours",
            minutesText = "Minutes",
            secondsText = "Seconds",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXCountdownTimerLivePreview() {
    SpaceXTheme {
        SpaceXCountdownTimer(
            countdown =
                CountdownData(
                    launchInstant = createPastInstant(minutes = 10),
                    launchName = "Falcon Heavy Demo",
                    isLive = true,
                ),
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
            countdownToLaunchText = "Countdown to Launch",
            countingText = "Counting",
            liveText = "LIVE",
            launchedText = "Launched",
            overdueText = "Overdue",
            daysText = "Days",
            hoursText = "Hours",
            minutesText = "Minutes",
            secondsText = "Seconds",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXCountdownDisplayPreview() {
    SpaceXTheme {
        SpaceXCountdownDisplay(
            timeRemaining =
                TimeRemaining(
                    days = 2,
                    hours = 5,
                    minutes = 30,
                    seconds = 45,
                ),
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            daysText = "Days",
            hoursText = "Hours",
            minutesText = "Minutes",
            secondsText = "Seconds",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXTimeUnitPreview() {
    SpaceXTheme {
        Row(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
        ) {
            SpaceXTimeUnit(
                value = 2,
                unit = "Days",
                textColor = SpaceXColors.OnSurface,
            )

            SpaceXTimeUnit(
                value = 5,
                unit = "Hours",
                textColor = SpaceXColors.OnSurface,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXCountdownStatusPreview() {
    SpaceXTheme {
        Column(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
        ) {
            SpaceXCountdownStatus(
                status = CountdownStatus.COUNTING,
                countingText = "Counting",
                liveText = "LIVE",
                launchedText = "Launched",
                overdueText = "Overdue",
            )

            SpaceXCountdownStatus(
                status = CountdownStatus.LIVE,
                countingText = "Counting",
                liveText = "LIVE",
                launchedText = "Launched",
                overdueText = "Overdue",
            )

            SpaceXCountdownStatus(
                status = CountdownStatus.LAUNCHED,
                countingText = "Counting",
                liveText = "LIVE",
                launchedText = "Launched",
                overdueText = "Overdue",
            )

            SpaceXCountdownStatus(
                status = CountdownStatus.OVERDUE,
                countingText = "Counting",
                liveText = "LIVE",
                launchedText = "Launched",
                overdueText = "Overdue",
            )
        }
    }
}
