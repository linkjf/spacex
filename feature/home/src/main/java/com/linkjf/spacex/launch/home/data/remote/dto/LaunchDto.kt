package com.linkjf.spacex.launch.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("date_utc")
    val dateUtc: String,
    @SerialName("rocket")
    val rocketId: String,
    @SerialName("launchpad")
    val launchpadId: String,
    @SerialName("links")
    val links: LaunchLinksDto? = null,
    @SerialName("details")
    val details: String? = null,
    @SerialName("success")
    val success: Boolean? = null,
    @SerialName("upcoming")
    val upcoming: Boolean,
    @SerialName("flight_number")
    val flightNumber: Int? = null,
    @SerialName("static_fire_date_utc")
    val staticFireDateUtc: String? = null,
    @SerialName("tbd")
    val tbd: Boolean? = null,
    @SerialName("net")
    val net: Boolean? = null,
    @SerialName("window")
    val window: Int? = null,
    @SerialName("payloads")
    val payloads: List<String>? = null,
    @SerialName("capsules")
    val capsules: List<String>? = null,
    @SerialName("ships")
    val ships: List<String>? = null,
    @SerialName("crew")
    val crew: List<String>? = null,
    @SerialName("cores")
    val cores: List<CoreDto>? = null,
    @SerialName("fairings")
    val fairings: FairingsDto? = null,
    @SerialName("auto_update")
    val autoUpdate: Boolean? = null,
    @SerialName("date_local")
    val dateLocal: String? = null,
    @SerialName("date_precision")
    val datePrecision: String? = null,
    @SerialName("date_unix")
    val dateUnix: Long? = null,
)

@Serializable
data class LaunchLinksDto(
    @SerialName("patch")
    val patch: LaunchPatchDto? = null,
    @SerialName("webcast")
    val webcast: String? = null,
    @SerialName("youtube_id")
    val youtubeId: String? = null,
    @SerialName("article")
    val article: String? = null,
    @SerialName("wikipedia")
    val wikipedia: String? = null,
)

@Serializable
data class LaunchPatchDto(
    @SerialName("small")
    val small: String? = null,
    @SerialName("large")
    val large: String? = null,
)
