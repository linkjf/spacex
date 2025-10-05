package com.linkjf.spacex.launch.design_system.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.design_system.R
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.CLOUD_CLEAR_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.CLOUD_MOSTLY_CLOUDY_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.CLOUD_PARTLY_CLOUDY_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.HUMIDITY_COMFORTABLE_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.HUMIDITY_DRY_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.HUMIDITY_HUMID_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.RAIN_LIGHT_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.RAIN_MODERATE_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.RAIN_NO_RAIN_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.TEMP_COLD_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.TEMP_COOL_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.TEMP_FREEZING_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.TEMP_WARM_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.VISIBILITY_FAIR_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.VISIBILITY_GOOD_MAX
import com.linkjf.spacex.launch.design_system.components.WeatherThresholds.VISIBILITY_POOR_MAX
import com.linkjf.spacex.launch.design_system.theme.SpaceXColors
import com.linkjf.spacex.launch.design_system.theme.SpaceXIcons
import com.linkjf.spacex.launch.design_system.theme.SpaceXSpacing
import com.linkjf.spacex.launch.design_system.theme.SpaceXTheme
import com.linkjf.spacex.launch.design_system.theme.SpaceXTypography

// Internal constants for weather thresholds
private object WeatherThresholds {
    // Cloud cover thresholds (%)
    const val CLOUD_CLEAR_MAX = 25f
    const val CLOUD_PARTLY_CLOUDY_MAX = 50f
    const val CLOUD_MOSTLY_CLOUDY_MAX = 75f

    // Rainfall thresholds (mm)
    const val RAIN_NO_RAIN_MAX = 0.1f
    const val RAIN_LIGHT_MAX = 2.5f
    const val RAIN_MODERATE_MAX = 10f

    // Temperature thresholds (°C)
    const val TEMP_FREEZING_MAX = 0f
    const val TEMP_COLD_MAX = 10f
    const val TEMP_COOL_MAX = 20f
    const val TEMP_WARM_MAX = 30f

    // Humidity thresholds (%)
    const val HUMIDITY_DRY_MAX = 30f
    const val HUMIDITY_COMFORTABLE_MAX = 60f
    const val HUMIDITY_HUMID_MAX = 80f

    // Visibility thresholds (km)
    const val VISIBILITY_POOR_MAX = 1f
    const val VISIBILITY_FAIR_MAX = 5f
    const val VISIBILITY_GOOD_MAX = 10f
}

data class WeatherData(
    val windSpeed: Float,
    val windDirection: Float,
    val cloudCover: Float,
    val rainfall: Float,
    val temperature: Float,
    val humidity: Float,
    val visibility: Float,
    val pressure: Float
)

@Composable
fun SpaceXWeatherInfo(
    weather: WeatherData,
    modifier: Modifier = Modifier,
    backgroundColor: Color = SpaceXColors.CardBackground,
    textColor: Color = SpaceXColors.OnSurface,
    dividerColor: Color = SpaceXColors.Divider,
    windSpeedUnit: String = stringResource(R.string.weather_wind_speed_unit_kmh),
    rainfallUnit: String = stringResource(R.string.weather_rainfall_unit_mm),
    temperatureUnit: String = stringResource(R.string.weather_temperature_unit_celsius),
    visibilityUnit: String = stringResource(R.string.weather_visibility_unit_km)
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(SpaceXSpacing.BorderRadiusMedium)
    ) {
        Column(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding)
        ) {
            Text(
                text = stringResource(R.string.weather_info_title),
                style = SpaceXTypography.Typography.titleLarge,
                color = textColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            HorizontalDivider(
                color = dividerColor,
                thickness = SpaceXSpacing.WeatherDividerThickness
            )

            Spacer(modifier = Modifier.height(SpaceXSpacing.Medium))

            Column(
                verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium)
                ) {
                    SpaceXWeatherMetric(
                        icon = SpaceXIcons.Wind,
                        label = stringResource(R.string.weather_wind_speed_label),
                        value = "${weather.windSpeed.toInt()} $windSpeedUnit",
                        subtitle = "${weather.windDirection.toInt()}°",
                        progress = weather.windSpeed / SpaceXSpacing.WeatherWindSpeedMax,
                        textColor = textColor,
                        modifier = Modifier.weight(1f)
                    )

                    SpaceXWeatherMetric(
                        icon = SpaceXIcons.Clouds,
                        label = stringResource(R.string.weather_cloud_cover_label),
                        value = "${weather.cloudCover.toInt()}%",
                        subtitle = getCloudCoverDescription(weather.cloudCover),
                        progress = weather.cloudCover / SpaceXSpacing.WeatherCloudCoverMax,
                        textColor = textColor,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium)
                ) {
                    SpaceXWeatherMetric(
                        icon = SpaceXIcons.Rain,
                        label = stringResource(R.string.weather_rainfall_label),
                        value = "${weather.rainfall}$rainfallUnit",
                        subtitle = getRainfallDescription(weather.rainfall),
                        progress = weather.rainfall / SpaceXSpacing.WeatherRainfallMax,
                        textColor = textColor,
                        modifier = Modifier.weight(1f)
                    )

                    SpaceXWeatherMetric(
                        icon = SpaceXIcons.Sun,
                        label = stringResource(R.string.weather_temperature_label),
                        value = "${weather.temperature.toInt()}$temperatureUnit",
                        subtitle = getTemperatureDescription(weather.temperature),
                        progress = (weather.temperature + SpaceXSpacing.WeatherTemperatureOffset) / SpaceXSpacing.WeatherTemperatureRange,
                        textColor = textColor,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium)
                ) {
                    SpaceXWeatherMetric(
                        icon = SpaceXIcons.Wind,
                        label = stringResource(R.string.weather_humidity_label),
                        value = "${weather.humidity.toInt()}%",
                        subtitle = getHumidityDescription(weather.humidity),
                        progress = weather.humidity / SpaceXSpacing.WeatherHumidityMax,
                        textColor = textColor,
                        modifier = Modifier.weight(1f)
                    )

                    SpaceXWeatherMetric(
                        icon = SpaceXIcons.Sun,
                        label = stringResource(R.string.weather_visibility_label),
                        value = "${weather.visibility}$visibilityUnit",
                        subtitle = getVisibilityDescription(weather.visibility),
                        progress = weather.visibility / SpaceXSpacing.WeatherVisibilityMax,
                        textColor = textColor,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceXWeatherMetric(
    icon: ImageVector,
    label: String,
    value: String,
    subtitle: String,
    progress: Float,
    modifier: Modifier = Modifier,
    textColor: Color = SpaceXColors.OnSurface,
    iconColor: Color = SpaceXColors.Primary,
    progressColor: Color = SpaceXColors.Primary
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(SpaceXSpacing.IconSmall)
            )

            Text(
                text = label,
                style = SpaceXTypography.Typography.labelMedium,
                color = SpaceXColors.OnSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            text = value,
            style = SpaceXTypography.Typography.titleLarge,
            color = textColor,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = subtitle,
            style = SpaceXTypography.Typography.bodySmall,
            color = SpaceXColors.OnSurfaceVariant
        )

        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(SpaceXSpacing.WeatherProgressIndicatorHeight)
                .clip(RoundedCornerShape(SpaceXSpacing.WeatherProgressIndicatorRadius)),
            color = progressColor,
            trackColor = SpaceXColors.SurfaceVariant,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
        )
    }
}

@Composable
private fun getCloudCoverDescription(cloudCover: Float): String {
    return when {
        cloudCover < CLOUD_CLEAR_MAX -> stringResource(R.string.weather_cloud_clear)
        cloudCover < CLOUD_PARTLY_CLOUDY_MAX -> stringResource(R.string.weather_cloud_partly_cloudy)
        cloudCover < CLOUD_MOSTLY_CLOUDY_MAX -> stringResource(R.string.weather_cloud_mostly_cloudy)
        else -> stringResource(R.string.weather_cloud_overcast)
    }
}

@Composable
private fun getRainfallDescription(rainfall: Float): String {
    return when {
        rainfall < RAIN_NO_RAIN_MAX -> stringResource(R.string.weather_rain_no_rain)
        rainfall < RAIN_LIGHT_MAX -> stringResource(R.string.weather_rain_light)
        rainfall < RAIN_MODERATE_MAX -> stringResource(R.string.weather_rain_moderate)
        else -> stringResource(R.string.weather_rain_heavy)
    }
}

@Composable
private fun getTemperatureDescription(temperature: Float): String {
    return when {
        temperature < TEMP_FREEZING_MAX -> stringResource(R.string.weather_temp_freezing)
        temperature < TEMP_COLD_MAX -> stringResource(R.string.weather_temp_cold)
        temperature < TEMP_COOL_MAX -> stringResource(R.string.weather_temp_cool)
        temperature < TEMP_WARM_MAX -> stringResource(R.string.weather_temp_warm)
        else -> stringResource(R.string.weather_temp_hot)
    }
}

@Composable
private fun getHumidityDescription(humidity: Float): String {
    return when {
        humidity < HUMIDITY_DRY_MAX -> stringResource(R.string.weather_humidity_dry)
        humidity < HUMIDITY_COMFORTABLE_MAX -> stringResource(R.string.weather_humidity_comfortable)
        humidity < HUMIDITY_HUMID_MAX -> stringResource(R.string.weather_humidity_humid)
        else -> stringResource(R.string.weather_humidity_very_humid)
    }
}

@Composable
private fun getVisibilityDescription(visibility: Float): String {
    return when {
        visibility < VISIBILITY_POOR_MAX -> stringResource(R.string.weather_visibility_poor)
        visibility < VISIBILITY_FAIR_MAX -> stringResource(R.string.weather_visibility_fair)
        visibility < VISIBILITY_GOOD_MAX -> stringResource(R.string.weather_visibility_good)
        else -> stringResource(R.string.weather_visibility_excellent)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXWeatherInfoPreview() {
    SpaceXTheme {
        SpaceXWeatherInfo(
            weather = WeatherData(
                windSpeed = 15.5f,
                windDirection = 245f,
                cloudCover = 35f,
                rainfall = 0.0f,
                temperature = 22f,
                humidity = 65f,
                visibility = 12f,
                pressure = 1013f
            ),
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
            windSpeedUnit = "km/h",
            rainfallUnit = "mm",
            temperatureUnit = "°C",
            visibilityUnit = "km"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXWeatherInfoStormyPreview() {
    SpaceXTheme {
        SpaceXWeatherInfo(
            weather = WeatherData(
                windSpeed = 45.2f,
                windDirection = 180f,
                cloudCover = 85f,
                rainfall = 8.5f,
                temperature = 18f,
                humidity = 90f,
                visibility = 3f,
                pressure = 995f
            ),
            modifier = Modifier.padding(SpaceXSpacing.CardMargin),
            windSpeedUnit = "mph",
            rainfallUnit = "in",
            temperatureUnit = "°F",
            visibilityUnit = "mi"
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXWeatherMetricPreview() {
    SpaceXTheme {
        Column(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium)
        ) {
            SpaceXWeatherMetric(
                icon = SpaceXIcons.Wind,
                label = "Wind Speed",
                value = "15 km/h",
                subtitle = "245°",
                progress = 0.3f,
                textColor = SpaceXColors.OnSurface
            )

            SpaceXWeatherMetric(
                icon = SpaceXIcons.Clouds,
                label = "Cloud Cover",
                value = "35%",
                subtitle = "Partly Cloudy",
                progress = 0.35f,
                textColor = SpaceXColors.OnSurface
            )
        }
    }
}
