package com.linkjf.spacex.launch.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DimensionDto(
    @SerialName("meters")
    val meters: Double? = null,
    @SerialName("feet")
    val feet: Double? = null,
)

@Serializable
data class MassDto(
    @SerialName("kg")
    val kg: Int,
    @SerialName("lb")
    val lb: Int,
)

@Serializable
data class PayloadWeightDto(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("kg")
    val kg: Int,
    @SerialName("lb")
    val lb: Int,
)

@Serializable
data class ThrustDto(
    @SerialName("kN")
    val kN: Int,
    @SerialName("lbf")
    val lbf: Int,
)

@Serializable
data class IspDto(
    @SerialName("sea_level")
    val seaLevel: Int,
    @SerialName("vacuum")
    val vacuum: Int,
)

@Serializable
data class StageDto(
    @SerialName("reusable")
    val reusable: Boolean,
    @SerialName("engines")
    val engines: Int,
    @SerialName("fuel_amount_tons")
    val fuelAmountTons: Double,
    @SerialName("burn_time_sec")
    val burnTimeSec: Int? = null,
    @SerialName("thrust_sea_level")
    val thrustSeaLevel: ThrustDto,
    @SerialName("thrust_vacuum")
    val thrustVacuum: ThrustDto,
)

@Serializable
data class EngineDto(
    @SerialName("isp")
    val isp: IspDto,
    @SerialName("thrust_sea_level")
    val thrustSeaLevel: ThrustDto,
    @SerialName("thrust_vacuum")
    val thrustVacuum: ThrustDto,
    @SerialName("number")
    val number: Int,
    @SerialName("type")
    val type: String,
    @SerialName("version")
    val version: String,
    @SerialName("layout")
    val layout: String,
    @SerialName("engine_loss_max")
    val engineLossMax: Int? = null,
    @SerialName("propellant_1")
    val propellant1: String,
    @SerialName("propellant_2")
    val propellant2: String,
    @SerialName("thrust_to_weight")
    val thrustToWeight: Double,
)

@Serializable
data class LandingLegsDto(
    @SerialName("number")
    val number: Int,
    @SerialName("material")
    val material: String? = null,
)
