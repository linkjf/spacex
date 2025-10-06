package com.linkjf.spacex.launch.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("type")
    val type: String,
    @SerialName("active")
    val active: Boolean,
    @SerialName("stages")
    val stages: Int,
    @SerialName("boosters")
    val boosters: Int,
    @SerialName("cost_per_launch")
    val costPerLaunch: Long,
    @SerialName("success_rate_pct")
    val successRatePct: Double,
    @SerialName("first_flight")
    val firstFlight: String,
    @SerialName("country")
    val country: String,
    @SerialName("company")
    val company: String,
    @SerialName("height")
    val height: DimensionDto,
    @SerialName("diameter")
    val diameter: DimensionDto,
    @SerialName("mass")
    val mass: MassDto,
    @SerialName("payload_weights")
    val payloadWeights: List<PayloadWeightDto>,
    @SerialName("first_stage")
    val firstStage: StageDto,
    @SerialName("second_stage")
    val secondStage: StageDto,
    @SerialName("engines")
    val engines: EngineDto,
    @SerialName("landing_legs")
    val landingLegs: LandingLegsDto,
    @SerialName("flickr_images")
    val flickrImages: List<String>,
    @SerialName("wikipedia")
    val wikipedia: String,
    @SerialName("description")
    val description: String,
)
