package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.*
import com.linkjf.spacex.launch.home.domain.model.*

object RocketMapper {
    fun mapToDomain(dto: RocketDto): Rocket =
        Rocket(
            id = dto.id,
            name = dto.name,
            type = dto.type,
            active = dto.active,
            stages = dto.stages,
            boosters = dto.boosters,
            costPerLaunch = dto.costPerLaunch,
            successRatePct = dto.successRatePct,
            firstFlight = dto.firstFlight,
            country = dto.country,
            company = dto.company,
            height = DimensionMapper.mapToDomain(dto.height),
            diameter = DimensionMapper.mapToDomain(dto.diameter),
            mass = MassMapper.mapToDomain(dto.mass),
            payloadWeights = dto.payloadWeights.map { PayloadWeightMapper.mapToDomain(it) },
            firstStage = StageMapper.mapToDomain(dto.firstStage),
            secondStage = StageMapper.mapToDomain(dto.secondStage),
            engines = EngineMapper.mapToDomain(dto.engines),
            landingLegs = LandingLegsMapper.mapToDomain(dto.landingLegs),
            flickrImages = dto.flickrImages,
            wikipedia = dto.wikipedia,
            description = dto.description,
        )

    fun mapToDomain(dtos: List<RocketDto>): List<Rocket> = dtos.map { mapToDomain(it) }
}
