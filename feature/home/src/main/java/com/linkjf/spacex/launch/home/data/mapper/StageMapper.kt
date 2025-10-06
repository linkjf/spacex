package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.StageDto
import com.linkjf.spacex.launch.home.domain.model.Stage

object StageMapper {
    fun mapToDomain(dto: StageDto): Stage =
        Stage(
            reusable = dto.reusable,
            engines = dto.engines,
            fuelAmountTons = dto.fuelAmountTons,
            burnTimeSec = dto.burnTimeSec,
            thrustSeaLevel = ThrustMapper.mapToDomain(dto.thrustSeaLevel),
            thrustVacuum = ThrustMapper.mapToDomain(dto.thrustVacuum),
        )
}
