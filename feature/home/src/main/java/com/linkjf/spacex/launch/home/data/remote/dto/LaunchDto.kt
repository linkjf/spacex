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
    val upcoming: Boolean
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
    val wikipedia: String? = null
)

@Serializable
data class LaunchPatchDto(
    @SerialName("small")
    val small: String? = null,
    
    @SerialName("large")
    val large: String? = null
)
