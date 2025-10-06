package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.repository.LaunchpadRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GetLaunchpadsUseCase
 */
@Singleton
class GetLaunchpadsUseCaseImpl
    @Inject
    constructor(
        private val launchpadRepository: LaunchpadRepository,
    ) : GetLaunchpadsUseCase {
        override suspend fun invoke(): Result<List<Launchpad>> = launchpadRepository.getLaunchpads()
    }
