package com.linkjf.spacex.launch.core.weather

// TODO remove after weater api integration
object WeatherDataGenerator {
    fun generateWeatherData(launchId: String): WeatherData {
        val hash = Math.abs(launchId.hashCode())
        val windSpeed = (30 + (hash % 40)).toString()
        val windGust = (50 + (hash % 50)).toString()
        val cloudCover = (10 + (hash % 50)).toString()
        val precipitationProb = (20 + (hash % 60)).toString()
        val visibility = String.format("%.1f", 1.0 + (hash % 10) / 10.0)
        val rainfall = if (hash % 10 < 2) "1.${hash % 10}mm" else "0.0mm"

        return WeatherData(
            windSpeed = "$windSpeed m/h",
            windGust = "${windGust}mix",
            cloudCover = "$cloudCover",
            precipitationProb = "${precipitationProb}ik",
            visibility = "${visibility}km",
            rainfall = rainfall,
        )
    }
}

data class WeatherData(
    val windSpeed: String,
    val windGust: String,
    val cloudCover: String,
    val precipitationProb: String,
    val visibility: String,
    val rainfall: String,
)
