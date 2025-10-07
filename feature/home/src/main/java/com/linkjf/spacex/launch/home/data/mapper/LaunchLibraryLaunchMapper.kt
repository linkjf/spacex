package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLibraryLaunchDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchPadDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchRocketDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchStatusDto
import com.linkjf.spacex.launch.home.domain.model.Dimension
import com.linkjf.spacex.launch.home.domain.model.Engine
import com.linkjf.spacex.launch.home.domain.model.Isp
import com.linkjf.spacex.launch.home.domain.model.LandingLegs
import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.LaunchLinks
import com.linkjf.spacex.launch.home.domain.model.LaunchPatch
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.model.Mass
import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.model.Stage
import com.linkjf.spacex.launch.home.domain.model.Thrust

object LaunchLibraryLaunchMapper {
    fun mapToDomain(
        dto: LaunchLibraryLaunchDto,
        isUpcoming: Boolean = true,
    ): Launch =
        Launch(
            id = dto.id,
            name = dto.name,
            dateUtc = dto.net,
            rocketId = dto.rocket.id.toString(),
            launchpadId = dto.pad.id.toString(),
            links = mapLinksToDomain(dto),
            details = dto.mission?.description,
            success = if (isUpcoming) null else mapSuccessFromStatus(dto.status),
            upcoming = isUpcoming,
            flightNumber = null,
            staticFireDateUtc = null,
            tbd = dto.status.name == "To Be Confirmed",
            net = dto.netPrecision?.name == "Minute",
            window = null,
            rocket = mapRocketToDomain(dto.rocket),
            launchpad = mapLaunchpadToDomain(dto.pad),
            payloads = null,
            capsules = null,
            ships = null,
            crew = null,
            cores = null,
            fairings = null,
            autoUpdate = null,
            dateLocal = null,
            datePrecision = dto.netPrecision?.name,
            dateUnix = null,
        )

    fun mapToDomain(
        dtos: List<LaunchLibraryLaunchDto>,
        isUpcoming: Boolean = true,
    ): List<Launch> = dtos.map { mapToDomain(it, isUpcoming) }

    private fun mapLinksToDomain(dto: LaunchLibraryLaunchDto): LaunchLinks? {
        val patchUrl = dto.image?.imageUrl

        return if (patchUrl != null) {
            LaunchLinks(
                patch =
                    LaunchPatch(
                        small = dto.image.thumbnailUrl ?: patchUrl,
                        large = patchUrl,
                    ),
                webcast = null,
                youtubeId = null,
                article = null,
                wikipedia = null,
            )
        } else {
            null
        }
    }

    private fun mapRocketToDomain(rocketDto: LaunchRocketDto): Rocket? =
        Rocket(
            id = rocketDto.id.toString(),
            name = rocketDto.configuration.name,
            type = rocketDto.configuration.fullName,
            active = true,
            stages = 2,
            boosters = 0,
            costPerLaunch = 0L,
            successRatePct = 0.0,
            firstFlight = "",
            country = "",
            company = "",
            height = Dimension(meters = null, feet = null),
            diameter = Dimension(meters = null, feet = null),
            mass = Mass(kg = 0, lb = 0),
            payloadWeights = emptyList(),
            firstStage =
                Stage(
                    reusable = false,
                    engines = 0,
                    fuelAmountTons = 0.0,
                    burnTimeSec = null,
                    thrustSeaLevel = Thrust(kN = 0, lbf = 0),
                    thrustVacuum = Thrust(kN = 0, lbf = 0),
                ),
            secondStage =
                Stage(
                    reusable = false,
                    engines = 0,
                    fuelAmountTons = 0.0,
                    burnTimeSec = null,
                    thrustSeaLevel = Thrust(kN = 0, lbf = 0),
                    thrustVacuum = Thrust(kN = 0, lbf = 0),
                ),
            engines =
                Engine(
                    isp = Isp(seaLevel = 0, vacuum = 0),
                    thrustSeaLevel = Thrust(kN = 0, lbf = 0),
                    thrustVacuum = Thrust(kN = 0, lbf = 0),
                    number = 0,
                    type = "",
                    version = "",
                    layout = "",
                    engineLossMax = null,
                    propellant1 = "",
                    propellant2 = "",
                    thrustToWeight = 0.0,
                ),
            landingLegs = LandingLegs(number = 0, material = null),
            flickrImages = emptyList(),
            wikipedia = "",
            description = "",
        )

    private fun mapLaunchpadToDomain(padDto: LaunchPadDto): Launchpad? =
        Launchpad(
            id = padDto.id.toString(),
            name = padDto.name,
            fullName = padDto.name,
            status = if (padDto.active == true) "active" else "inactive",
            locality = padDto.location.name,
            region = padDto.location.country.name,
            timezone = padDto.location.timezoneName ?: "",
            latitude = padDto.latitude ?: 0.0,
            longitude = padDto.longitude ?: 0.0,
            launchAttempts = padDto.totalLaunchCount,
            launchSuccesses = padDto.orbitalLaunchAttemptCount,
            rockets = emptyList(),
            launches = emptyList(),
            details = padDto.description ?: "",
        )

    private fun mapSuccessFromStatus(status: LaunchStatusDto): Boolean? =
        when (status.name.lowercase()) {
            "launch successful" -> true
            "launch failure", "partial failure" -> false
            else -> null
        }
}
