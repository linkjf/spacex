package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.FairingsDto
import com.linkjf.spacex.launch.home.domain.model.Fairings

object FairingsMapper {
    fun mapToDomain(dto: FairingsDto): Fairings =
        Fairings(
            reused = dto.reused,
            recoveryAttempt = dto.recoveryAttempt,
            recovered = dto.recovered,
            ships = dto.ships,
        )
}
