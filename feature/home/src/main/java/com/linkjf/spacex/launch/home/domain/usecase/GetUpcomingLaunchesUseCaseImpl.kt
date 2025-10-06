package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GetUpcomingLaunchesUseCase
 */
@Singleton
class GetUpcomingLaunchesUseCaseImpl
    @Inject
    constructor(
        private val launchRepository: LaunchRepository,
    ) : GetUpcomingLaunchesUseCase {
        override suspend fun invoke(): Result<List<Launch>> = launchRepository.getUpcomingLaunches()
    }
