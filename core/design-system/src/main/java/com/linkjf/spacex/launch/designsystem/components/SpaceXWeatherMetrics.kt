package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.designsystem.R
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXIcons
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography

data class WeatherMetric(
    val label: String,
    val value: String,
    val icon: ImageVector? = null,
)

@Composable
fun SpaceXWeatherMetrics(
    metrics: List<WeatherMetric>,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    iconColor: Color = SpaceXColors.OnSurface,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium2),
    ) {
        metrics.forEach { metric ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
            ) {
                metric.icon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = metric.label,
                        tint = iconColor,
                        modifier = Modifier.size(SpaceXSpacing.IconSmall),
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Micro),
                ) {
                    Text(
                        text = metric.value,
                        style = SpaceXTypography.Typography.titleLarge,
                        color = textColor,
                    )
                    Text(
                        text = metric.label,
                        style = SpaceXTypography.Typography.titleLarge,
                        color = textColor,
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceXWeatherMetric(
    value: String,
    label: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    iconColor: Color = SpaceXColors.OnSurface,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
    ) {
        icon?.let { imageVector ->
            Icon(
                imageVector = imageVector,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(SpaceXSpacing.IconSmall),
            )
        }
        Text(
            text = label,
            style = SpaceXTypography.weatherMetric,
            color = textColor,
        )
        Text(
            text = value,
            style = SpaceXTypography.weatherMetric,
            color = textColor,
        )
    }
}

@Composable
fun SpaceXWeatherMetricsRow(
    windSpeed: String,
    cloudCover: String,
    rainfall: String,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    iconColor: Color = SpaceXColors.OnSurface,
    windLabel: String = stringResource(R.string.weather_metrics_wind_label),
    cloudsLabel: String = stringResource(R.string.weather_metrics_clouds_label),
    rainLabel: String = stringResource(R.string.weather_metrics_rain_label),
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium2),
    ) {
        SpaceXWeatherMetric(
            value = windSpeed,
            label = windLabel,
            icon = SpaceXIcons.Wind,
            textColor = textColor,
            iconColor = iconColor,
        )

        SpaceXWeatherMetric(
            value = cloudCover,
            label = cloudsLabel,
            icon = SpaceXIcons.Clouds,
            textColor = textColor,
            iconColor = iconColor,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXWeatherMetricsPreview() {
    SpaceXTheme {
        SpaceXWeatherMetricsRow(
            windSpeed = "60%",
            cloudCover = "35%",
            rainfall = "0.0mm",
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            windLabel = "Wind",
            cloudsLabel = "Clouds",
            rainLabel = "Rain",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXWeatherMetricsCustomPreview() {
    SpaceXTheme {
        val customMetrics =
            listOf(
                WeatherMetric(
                    label = "Wind",
                    value = "45%",
                    icon = SpaceXIcons.Wind,
                ),
                WeatherMetric(
                    label = "Clouds",
                    value = "80%",
                    icon = SpaceXIcons.Clouds,
                ),
                WeatherMetric(
                    label = "Rain",
                    value = "2.5mm",
                    icon = SpaceXIcons.Rain,
                ),
                WeatherMetric(
                    label = "Sun",
                    value = "90%",
                    icon = SpaceXIcons.Sun,
                ),
            )

        SpaceXWeatherMetrics(
            metrics = customMetrics,
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
        )
    }
}
