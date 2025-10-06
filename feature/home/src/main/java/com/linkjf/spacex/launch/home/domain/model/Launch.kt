package com.linkjf.spacex.launch.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Launch(
    val id: String,
    val name: String,
    val dateUtc: String,
    val rocketId: String,
    val launchpadId: String,
    val links: LaunchLinks? = null,
    val details: String? = null,
    val success: Boolean? = null,
    val upcoming: Boolean,
    val flightNumber: Int? = null,
    val staticFireDateUtc: String? = null,
    val tbd: Boolean? = null,
    val net: Boolean? = null,
    val window: Int? = null,
    val rocket: Rocket? = null,
    val launchpad: Launchpad? = null,
    val payloads: List<String>? = null,
    val capsules: List<String>? = null,
    val ships: List<String>? = null,
    val crew: List<String>? = null,
    val cores: List<Core>? = null,
    val fairings: Fairings? = null,
    val autoUpdate: Boolean? = null,
    val dateLocal: String? = null,
    val datePrecision: String? = null,
    val dateUnix: Long? = null,
)

@Serializable
data class LaunchLinks(
    val patch: LaunchPatch? = null,
    val webcast: String? = null,
    val youtubeId: String? = null,
    val article: String? = null,
    val wikipedia: String? = null,
)

@Serializable
data class LaunchPatch(
    val small: String? = null,
    val large: String? = null,
)
