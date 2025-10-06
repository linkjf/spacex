package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.MassDto
import com.linkjf.spacex.launch.home.domain.model.Mass

object MassMapper {
    fun mapToDomain(dto: MassDto): Mass =
        Mass(
            kg = dto.kg,
            lb = dto.lb,
        )
}
