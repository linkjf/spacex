package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.LaunchpadDto
import com.linkjf.spacex.launch.home.domain.model.Launchpad

object LaunchpadMapper {
    fun mapToDomain(dto: LaunchpadDto): Launchpad =
        Launchpad(
            id = dto.id,
            name = dto.name,
            fullName = dto.fullName,
            status = dto.status,
            locality = dto.locality,
            region = dto.region,
            timezone = dto.timezone,
            latitude = dto.latitude,
            longitude = dto.longitude,
            launchAttempts = dto.launchAttempts,
            launchSuccesses = dto.launchSuccesses,
            rockets = dto.rockets,
            launches = dto.launches,
            details = dto.details,
        )

    fun mapToDomain(dtos: List<LaunchpadDto>): List<Launchpad> = dtos.map { mapToDomain(it) }
}
