package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.LaunchpadMapper
import com.linkjf.spacex.launch.home.data.remote.SpaceXApi
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchpadDto
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.repository.LaunchpadRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchpadRepositoryImpl
    @Inject
    constructor(
        private val spaceXApi: SpaceXApi,
    ) : LaunchpadRepository {
        override suspend fun getLaunchpads(): Result<List<Launchpad>> =
            try {
                val launchpadDtos = spaceXApi.getLaunchpads()
                val launchpads = LaunchpadMapper.mapToDomain(launchpadDtos)
                Result.success(launchpads)
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun getLaunchpad(id: String): Result<Launchpad> =
            try {
                val launchpadDto = spaceXApi.getLaunchpad(id)
                val launchpad = LaunchpadMapper.mapToDomain(launchpadDto)
                Result.success(launchpad)
            } catch (e: Exception) {
                Result.failure(e)
            }
    }
