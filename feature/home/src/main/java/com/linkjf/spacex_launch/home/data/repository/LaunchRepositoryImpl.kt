package com.linkjf.spacex_launch.home.data.repository

import com.linkjf.spacex_launch.home.data.mapper.LaunchMapper
import com.linkjf.spacex_launch.home.data.remote.SpaceXApi
import com.linkjf.spacex_launch.home.domain.model.Launch
import com.linkjf.spacex_launch.home.domain.repository.LaunchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRepositoryImpl @Inject constructor(
    private val spaceXApi: SpaceXApi
) : LaunchRepository {
    
    override suspend fun getUpcomingLaunches(): Result<List<Launch>> {
        return try {
            val launchDtos = spaceXApi.getUpcomingLaunches()
            val launches = LaunchMapper.mapToDomain(launchDtos)
            Result.success(launches)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
