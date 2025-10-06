package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.repository.RocketRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GetRocketsUseCase
 */
@Singleton
class GetRocketsUseCaseImpl
    @Inject
    constructor(
        private val rocketRepository: RocketRepository,
    ) : GetRocketsUseCase {
        override suspend fun invoke(): Result<List<Rocket>> = rocketRepository.getRockets()
    }
