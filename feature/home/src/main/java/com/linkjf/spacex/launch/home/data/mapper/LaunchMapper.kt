package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.*
import com.linkjf.spacex.launch.home.domain.model.*

object LaunchMapper {
    fun mapToDomain(dto: LaunchDto): Launch =
        Launch(
            id = dto.id,
            name = dto.name,
            dateUtc = dto.dateUtc,
            rocketId = dto.rocketId,
            launchpadId = dto.launchpadId,
            links = dto.links?.let { mapLinksToDomain(it) },
            details = dto.details,
            success = dto.success,
            upcoming = dto.upcoming,
            flightNumber = dto.flightNumber,
            staticFireDateUtc = dto.staticFireDateUtc,
            tbd = dto.tbd,
            net = dto.net,
            window = dto.window,
            rocket = null, // Will be populated separately via API calls
            launchpad = null, // Will be populated separately via API calls
            payloads = dto.payloads,
            capsules = dto.capsules,
            ships = dto.ships,
            crew = dto.crew,
            cores = dto.cores?.map { CoreMapper.mapToDomain(it) },
            fairings = dto.fairings?.let { FairingsMapper.mapToDomain(it) },
            autoUpdate = dto.autoUpdate,
            dateLocal = dto.dateLocal,
            datePrecision = dto.datePrecision,
            dateUnix = dto.dateUnix,
        )

    fun mapToDomain(dtos: List<LaunchDto>): List<Launch> = dtos.map { mapToDomain(it) }

    private fun mapLinksToDomain(dto: LaunchLinksDto): LaunchLinks =
        LaunchLinks(
            patch = dto.patch?.let { mapPatchToDomain(it) },
            webcast = dto.webcast,
            youtubeId = dto.youtubeId,
            article = dto.article,
            wikipedia = dto.wikipedia,
        )

    private fun mapPatchToDomain(dto: LaunchPatchDto): LaunchPatch =
        LaunchPatch(
            small = dto.small,
            large = dto.large,
        )
}
