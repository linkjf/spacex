package com.linkjf.spacex.launch.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoreDto(
    @SerialName("core")
    val core: String,
    @SerialName("flight")
    val flight: Int,
    @SerialName("gridfins")
    val gridfins: Boolean,
    @SerialName("legs")
    val legs: Boolean,
    @SerialName("reused")
    val reused: Boolean,
    @SerialName("landing_attempt")
    val landingAttempt: Boolean,
    @SerialName("landing_success")
    val landingSuccess: Boolean? = null,
    @SerialName("landing_type")
    val landingType: String? = null,
    @SerialName("landpad")
    val landpad: String? = null,
)

@Serializable
data class FairingsDto(
    @SerialName("reused")
    val reused: Boolean? = null,
    @SerialName("recovery_attempt")
    val recoveryAttempt: Boolean? = null,
    @SerialName("recovered")
    val recovered: Boolean? = null,
    @SerialName("ships")
    val ships: List<String>,
)
