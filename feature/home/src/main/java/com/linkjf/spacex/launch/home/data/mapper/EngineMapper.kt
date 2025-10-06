package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.EngineDto
import com.linkjf.spacex.launch.home.domain.model.Engine

object EngineMapper {
    fun mapToDomain(dto: EngineDto): Engine =
        Engine(
            isp = IspMapper.mapToDomain(dto.isp),
            thrustSeaLevel = ThrustMapper.mapToDomain(dto.thrustSeaLevel),
            thrustVacuum = ThrustMapper.mapToDomain(dto.thrustVacuum),
            number = dto.number,
            type = dto.type,
            version = dto.version,
            layout = dto.layout,
            engineLossMax = dto.engineLossMax,
            propellant1 = dto.propellant1,
            propellant2 = dto.propellant2,
            thrustToWeight = dto.thrustToWeight,
        )
}
