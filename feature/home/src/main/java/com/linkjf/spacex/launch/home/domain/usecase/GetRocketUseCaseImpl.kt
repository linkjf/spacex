package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.repository.RocketRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GetRocketUseCase
 */
@Singleton
class GetRocketUseCaseImpl
    @Inject
    constructor(
        private val rocketRepository: RocketRepository,
    ) : GetRocketUseCase {
        override suspend fun invoke(id: String): Result<Rocket> = rocketRepository.getRocket(id)
    }
