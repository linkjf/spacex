package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.LaunchLibraryLaunchMapper
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRepositoryImpl
    @Inject
    constructor(
        private val launchLibraryApi: LaunchLibraryApi,
    ) : LaunchRepository {
        override suspend fun getUpcomingLaunches(): Result<List<Launch>> =
            try {
                val response = launchLibraryApi.getUpcomingLaunches()
                val launches = LaunchLibraryLaunchMapper.mapToDomain(response.results, isUpcoming = true)
                Result.success(launches)
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun getPastLaunches(): Result<List<Launch>> =
            try {
                val response = launchLibraryApi.getPastLaunches()
                val launches = LaunchLibraryLaunchMapper.mapToDomain(response.results, isUpcoming = false)
                Result.success(launches)
            } catch (e: Exception) {
                Result.failure(e)
            }
    }
