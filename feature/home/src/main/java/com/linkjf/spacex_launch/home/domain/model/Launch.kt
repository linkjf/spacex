package com.linkjf.spacex_launch.home.domain.model

data class Launch(
    val id: String,
    val name: String,
    val dateUtc: String,
    val rocketId: String,
    val launchpadId: String,
    val links: LaunchLinks? = null,
    val details: String? = null,
    val success: Boolean? = null,
    val upcoming: Boolean
)

data class LaunchLinks(
    val patch: LaunchPatch? = null,
    val webcast: String? = null,
    val youtubeId: String? = null,
    val article: String? = null,
    val wikipedia: String? = null
)

data class LaunchPatch(
    val small: String? = null,
    val large: String? = null
)
