package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.ThrustDto
import com.linkjf.spacex.launch.home.domain.model.Thrust

object ThrustMapper {
    fun mapToDomain(dto: ThrustDto): Thrust =
        Thrust(
            kN = dto.kN,
            lbf = dto.lbf,
        )
}
