package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.PayloadWeightDto
import com.linkjf.spacex.launch.home.domain.model.PayloadWeight

object PayloadWeightMapper {
    fun mapToDomain(dto: PayloadWeightDto): PayloadWeight =
        PayloadWeight(
            id = dto.id,
            name = dto.name,
            kg = dto.kg,
            lb = dto.lb,
        )
}
