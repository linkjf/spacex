package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.core.time.TimeUtils.calculateCountdown
import com.linkjf.spacex.launch.core.time.TimeUtils.formatToLaunchDate
import com.linkjf.spacex.launch.core.time.TimeUtils.formatToLaunchTime
import com.linkjf.spacex.launch.core.weather.WeatherDataGenerator
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.home.domain.model.Launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchToLaunchListItemMapper
    @Inject
    constructor() {
        fun mapToLaunchListItem(launch: Launch): LaunchListItem {
            val weatherData = WeatherDataGenerator.generateWeatherData(launch.id)
            val formattedDate = launch.dateUtc.formatToLaunchDate()
            val formattedTime = launch.dateUtc.formatToLaunchTime()
            val countdown = launch.dateUtc.calculateCountdown(launch.upcoming)

            return LaunchListItem(
                id = launch.id,
                name = launch.name,
                date = formattedDate,
                time = formattedTime,
                rocketId = launch.rocketId,
                launchpadId = launch.launchpadId,
                patchImageUrl = launch.links?.patch?.small,
                windSpeed = weatherData.windSpeed,
                cloudCover = weatherData.cloudCover,
                rainfall = weatherData.rainfall,
                countdown = countdown,
                isUpcoming = launch.upcoming,
            )
        }

        fun mapToLaunchListItemList(launches: List<Launch>): List<LaunchListItem> = launches.map { mapToLaunchListItem(it) }
    }
