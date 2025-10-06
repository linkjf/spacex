package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.DimensionDto
import com.linkjf.spacex.launch.home.domain.model.Dimension

object DimensionMapper {
    fun mapToDomain(dto: DimensionDto): Dimension =
        Dimension(
            meters = dto.meters,
            feet = dto.feet,
        )
}
