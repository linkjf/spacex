package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.LandingLegsDto
import com.linkjf.spacex.launch.home.domain.model.LandingLegs

object LandingLegsMapper {
    fun mapToDomain(dto: LandingLegsDto): LandingLegs =
        LandingLegs(
            number = dto.number,
            material = dto.material,
        )
}
