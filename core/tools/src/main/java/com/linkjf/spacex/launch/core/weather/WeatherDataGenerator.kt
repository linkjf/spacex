package com.linkjf.spacex.launch.core.weather

// TODO remove after weater api integration
object WeatherDataGenerator {
    fun generateWeatherData(launchId: String): WeatherData {
        val hash = Math.abs(launchId.hashCode())
        val windSpeed = (30 + (hash % 40)).toString()
        val cloudCover = (10 + (hash % 50)).toString() + "%"
        val rainfall = if (hash % 10 < 2) "1.${hash % 10}mm" else "0.0mm"

        return WeatherData(
            windSpeed = "$windSpeed m/h",
            cloudCover = cloudCover,
            rainfall = rainfall,
        )
    }
}

data class WeatherData(
    val windSpeed: String,
    val cloudCover: String,
    val rainfall: String,
)
