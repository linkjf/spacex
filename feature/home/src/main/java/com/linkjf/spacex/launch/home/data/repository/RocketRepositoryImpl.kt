package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.RocketMapper
import com.linkjf.spacex.launch.home.data.remote.SpaceXApi
import com.linkjf.spacex.launch.home.data.remote.dto.RocketDto
import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.repository.RocketRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RocketRepositoryImpl
    @Inject
    constructor(
        private val spaceXApi: SpaceXApi,
    ) : RocketRepository {
        override suspend fun getRockets(): Result<List<Rocket>> =
            try {
                val rocketDtos = spaceXApi.getRockets()
                val rockets = RocketMapper.mapToDomain(rocketDtos)
                Result.success(rockets)
            } catch (e: Exception) {
                Result.failure(e)
            }

        override suspend fun getRocket(id: String): Result<Rocket> =
            try {
                val rocketDto = spaceXApi.getRocket(id)
                val rocket = RocketMapper.mapToDomain(rocketDto)
                Result.success(rocket)
            } catch (e: Exception) {
                Result.failure(e)
            }
    }
