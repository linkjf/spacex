package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.database.entity.LaunchEntity
import com.linkjf.spacex.launch.database.entity.LaunchpadEntity
import com.linkjf.spacex.launch.database.entity.RocketEntity
import com.linkjf.spacex.launch.home.domain.model.Core
import com.linkjf.spacex.launch.home.domain.model.Dimension
import com.linkjf.spacex.launch.home.domain.model.Engine
import com.linkjf.spacex.launch.home.domain.model.Fairings
import com.linkjf.spacex.launch.home.domain.model.Isp
import com.linkjf.spacex.launch.home.domain.model.LandingLegs
import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.LaunchLinks
import com.linkjf.spacex.launch.home.domain.model.LaunchPatch
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.model.Mass
import com.linkjf.spacex.launch.home.domain.model.PayloadWeight
import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.model.Stage
import com.linkjf.spacex.launch.home.domain.model.Thrust
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Mappers to convert between domain models and database entities
 */
object DatabaseMapper {
    private val json = Json { ignoreUnknownKeys = true }

    // Launch mappers
    fun LaunchEntity.toDomain(): Launch =
        Launch(
            id = id,
            name = name,
            dateUtc = dateUtc,
            rocketId = rocketId,
            launchpadId = launchpadId,
            details = details,
            success = success,
            upcoming = upcoming,
            flightNumber = flightNumber,
            staticFireDateUtc = staticFireDateUtc,
            tbd = tbd,
            net = net,
            window = window,
            payloads = payloads?.let { parseStringList(it) },
            capsules = capsules?.let { parseStringList(it) },
            ships = ships?.let { parseStringList(it) },
            crew = crew?.let { parseStringList(it) },
            cores = cores?.let { parseCoreList(it) },
            fairings = fairings?.let { parseFairings(it) },
            autoUpdate = autoUpdate,
            dateLocal = dateLocal,
            datePrecision = datePrecision,
            dateUnix = dateUnix,
            links = links?.let { parseLaunchLinks(it) },
        )

    fun Launch.toEntity(): LaunchEntity =
        LaunchEntity(
            id = id,
            name = name,
            dateUtc = dateUtc,
            rocketId = rocketId,
            launchpadId = launchpadId,
            details = details,
            success = success,
            upcoming = upcoming,
            flightNumber = flightNumber,
            staticFireDateUtc = staticFireDateUtc,
            tbd = tbd,
            net = net,
            window = window,
            payloads = payloads?.let { json.encodeToString(it) },
            capsules = capsules?.let { json.encodeToString(it) },
            ships = ships?.let { json.encodeToString(it) },
            crew = crew?.let { json.encodeToString(it) },
            cores = cores?.let { json.encodeToString(it) },
            fairings = fairings?.let { json.encodeToString(it) },
            autoUpdate = autoUpdate,
            dateLocal = dateLocal,
            datePrecision = datePrecision,
            dateUnix = dateUnix,
            links = links?.let { json.encodeToString(it) },
        )

    // Rocket mappers
    fun RocketEntity.toDomain(): Rocket =
        Rocket(
            id = id,
            name = name,
            type = type,
            active = active,
            stages = stages,
            boosters = boosters,
            costPerLaunch = costPerLaunch,
            successRatePct = successRatePct,
            firstFlight = firstFlight,
            country = country,
            company = company,
            height = height?.let { parseDimension(it) } ?: Dimension(),
            diameter = diameter?.let { parseDimension(it) } ?: Dimension(),
            mass = mass?.let { parseMass(it) } ?: Mass(0, 0),
            payloadWeights = payloadWeights?.let { parsePayloadWeightList(it) } ?: emptyList(),
            firstStage = firstStage?.let { parseStage(it) } ?: Stage(false, 0, 0.0, null, Thrust(0, 0), Thrust(0, 0)),
            secondStage = secondStage?.let { parseStage(it) } ?: Stage(false, 0, 0.0, null, Thrust(0, 0), Thrust(0, 0)),
            engines = engines?.let { parseEngine(it) } ?: Engine(Isp(0, 0), Thrust(0, 0), Thrust(0, 0), 0, "", "", "", null, "", "", 0.0),
            landingLegs = landingLegs?.let { parseLandingLegs(it) } ?: LandingLegs(0),
            flickrImages = flickrImages?.let { parseStringList(it) } ?: emptyList(),
            wikipedia = wikipedia,
            description = description,
        )

    fun Rocket.toEntity(): RocketEntity =
        RocketEntity(
            id = id,
            name = name,
            type = type,
            active = active,
            stages = stages,
            boosters = boosters,
            costPerLaunch = costPerLaunch,
            successRatePct = successRatePct,
            firstFlight = firstFlight,
            country = country,
            company = company,
            height = json.encodeToString(height),
            diameter = json.encodeToString(diameter),
            mass = json.encodeToString(mass),
            payloadWeights = json.encodeToString(payloadWeights),
            firstStage = json.encodeToString(firstStage),
            secondStage = json.encodeToString(secondStage),
            engines = json.encodeToString(engines),
            landingLegs = json.encodeToString(landingLegs),
            flickrImages = json.encodeToString(flickrImages),
            wikipedia = wikipedia,
            description = description,
        )

    // Launchpad mappers
    fun LaunchpadEntity.toDomain(): Launchpad =
        Launchpad(
            id = id,
            name = name,
            fullName = fullName,
            status = status,
            locality = locality,
            region = region,
            timezone = timezone,
            latitude = latitude,
            longitude = longitude,
            launchAttempts = launchAttempts,
            launchSuccesses = launchSuccesses,
            rockets = rockets?.let { parseStringList(it) } ?: emptyList(),
            launches = launches?.let { parseStringList(it) } ?: emptyList(),
            details = details,
        )

    fun Launchpad.toEntity(): LaunchpadEntity =
        LaunchpadEntity(
            id = id,
            name = name,
            fullName = fullName,
            status = status,
            locality = locality,
            region = region,
            timezone = timezone,
            latitude = latitude,
            longitude = longitude,
            launchAttempts = launchAttempts,
            launchSuccesses = launchSuccesses,
            rockets = json.encodeToString(rockets),
            launches = json.encodeToString(launches),
            details = details,
        )

    // Helper parsing functions
    private fun parseStringList(jsonString: String): List<String> =
        try {
            json.decodeFromString<List<String>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }

    private fun parseCoreList(jsonString: String): List<Core> =
        try {
            json.decodeFromString<List<Core>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }

    private fun parseFairings(jsonString: String): Fairings =
        try {
            json.decodeFromString<Fairings>(jsonString)
        } catch (e: Exception) {
            Fairings(ships = emptyList())
        }

    private fun parseLaunchLinks(jsonString: String): LaunchLinks =
        try {
            json.decodeFromString<LaunchLinks>(jsonString)
        } catch (e: Exception) {
            LaunchLinks()
        }

    private fun parseDimension(jsonString: String): Dimension =
        try {
            json.decodeFromString<Dimension>(jsonString)
        } catch (e: Exception) {
            Dimension()
        }

    private fun parseMass(jsonString: String): Mass =
        try {
            json.decodeFromString<Mass>(jsonString)
        } catch (e: Exception) {
            Mass(0, 0)
        }

    private fun parsePayloadWeightList(jsonString: String): List<PayloadWeight> =
        try {
            json.decodeFromString<List<PayloadWeight>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }

    private fun parseStage(jsonString: String): Stage =
        try {
            json.decodeFromString<Stage>(jsonString)
        } catch (e: Exception) {
            Stage(false, 0, 0.0, null, Thrust(0, 0), Thrust(0, 0))
        }

    private fun parseEngine(jsonString: String): Engine =
        try {
            json.decodeFromString<Engine>(jsonString)
        } catch (e: Exception) {
            Engine(Isp(0, 0), Thrust(0, 0), Thrust(0, 0), 0, "", "", "", null, "", "", 0.0)
        }

    private fun parseLandingLegs(jsonString: String): LandingLegs =
        try {
            json.decodeFromString<LandingLegs>(jsonString)
        } catch (e: Exception) {
            LandingLegs(0)
        }
}
