package com.linkjf.spacex.launch.home.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Rocket(
    val id: String,
    val name: String,
    val type: String,
    val active: Boolean,
    val stages: Int,
    val boosters: Int,
    val costPerLaunch: Long,
    val successRatePct: Double,
    val firstFlight: String,
    val country: String,
    val company: String,
    val height: Dimension,
    val diameter: Dimension,
    val mass: Mass,
    val payloadWeights: List<PayloadWeight>,
    val firstStage: Stage,
    val secondStage: Stage,
    val engines: Engine,
    val landingLegs: LandingLegs,
    val flickrImages: List<String>,
    val wikipedia: String,
    val description: String,
)

@Serializable
data class Launchpad(
    val id: String,
    val name: String,
    val fullName: String,
    val status: String,
    val locality: String,
    val region: String,
    val timezone: String,
    val latitude: Double,
    val longitude: Double,
    val launchAttempts: Int,
    val launchSuccesses: Int,
    val rockets: List<String>,
    val launches: List<String>,
    val details: String,
)

@Serializable
data class Core(
    val core: String,
    val flight: Int,
    val gridfins: Boolean,
    val legs: Boolean,
    val reused: Boolean,
    val landingAttempt: Boolean,
    val landingSuccess: Boolean? = null,
    val landingType: String? = null,
    val landpad: String? = null,
)

@Serializable
data class Fairings(
    val reused: Boolean? = null,
    val recoveryAttempt: Boolean? = null,
    val recovered: Boolean? = null,
    val ships: List<String>,
)

@Serializable
data class Dimension(
    val meters: Double? = null,
    val feet: Double? = null,
)

@Serializable
data class Mass(
    val kg: Int,
    val lb: Int,
)

@Serializable
data class PayloadWeight(
    val id: String,
    val name: String,
    val kg: Int,
    val lb: Int,
)

@Serializable
data class Thrust(
    val kN: Int,
    val lbf: Int,
)

@Serializable
data class Isp(
    val seaLevel: Int,
    val vacuum: Int,
)

@Serializable
data class Stage(
    val reusable: Boolean,
    val engines: Int,
    val fuelAmountTons: Double,
    val burnTimeSec: Int? = null,
    val thrustSeaLevel: Thrust,
    val thrustVacuum: Thrust,
)

@Serializable
data class Engine(
    val isp: Isp,
    val thrustSeaLevel: Thrust,
    val thrustVacuum: Thrust,
    val number: Int,
    val type: String,
    val version: String,
    val layout: String,
    val engineLossMax: Int? = null,
    val propellant1: String,
    val propellant2: String,
    val thrustToWeight: Double,
)

@Serializable
data class LandingLegs(
    val number: Int,
    val material: String? = null,
)
