package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.CoreDto
import com.linkjf.spacex.launch.home.domain.model.Core

object CoreMapper {
    fun mapToDomain(dto: CoreDto): Core =
        Core(
            core = dto.core,
            flight = dto.flight,
            gridfins = dto.gridfins,
            legs = dto.legs,
            reused = dto.reused,
            landingAttempt = dto.landingAttempt,
            landingSuccess = dto.landingSuccess,
            landingType = dto.landingType,
            landpad = dto.landpad,
        )

    fun mapToDomain(dtos: List<CoreDto>): List<Core> = dtos.map { mapToDomain(it) }
}
