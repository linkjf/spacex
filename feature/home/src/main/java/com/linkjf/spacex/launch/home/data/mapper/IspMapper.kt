package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.IspDto
import com.linkjf.spacex.launch.home.domain.model.Isp

object IspMapper {
    fun mapToDomain(dto: IspDto): Isp =
        Isp(
            seaLevel = dto.seaLevel,
            vacuum = dto.vacuum,
        )
}
