package com.linkjf.spacex_launch.home.data.mapper

import com.linkjf.spacex_launch.home.data.remote.dto.LaunchDto
import com.linkjf.spacex_launch.home.data.remote.dto.LaunchLinksDto
import com.linkjf.spacex_launch.home.data.remote.dto.LaunchPatchDto
import com.linkjf.spacex_launch.home.domain.model.Launch
import com.linkjf.spacex_launch.home.domain.model.LaunchLinks
import com.linkjf.spacex_launch.home.domain.model.LaunchPatch

object LaunchMapper {
    
    fun mapToDomain(dto: LaunchDto): Launch {
        return Launch(
            id = dto.id,
            name = dto.name,
            dateUtc = dto.dateUtc,
            rocketId = dto.rocketId,
            launchpadId = dto.launchpadId,
            links = dto.links?.let { mapLinksToDomain(it) },
            details = dto.details,
            success = dto.success,
            upcoming = dto.upcoming
        )
    }
    
    fun mapToDomain(dtos: List<LaunchDto>): List<Launch> {
        return dtos.map { mapToDomain(it) }
    }
    
    private fun mapLinksToDomain(dto: LaunchLinksDto): LaunchLinks {
        return LaunchLinks(
            patch = dto.patch?.let { mapPatchToDomain(it) },
            webcast = dto.webcast,
            youtubeId = dto.youtubeId,
            article = dto.article,
            wikipedia = dto.wikipedia
        )
    }
    
    private fun mapPatchToDomain(dto: LaunchPatchDto): LaunchPatch {
        return LaunchPatch(
            small = dto.small,
            large = dto.large
        )
    }
}
