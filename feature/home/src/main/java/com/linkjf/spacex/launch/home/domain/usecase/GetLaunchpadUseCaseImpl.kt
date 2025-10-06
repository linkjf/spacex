package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.repository.LaunchpadRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GetLaunchpadUseCase
 */
@Singleton
class GetLaunchpadUseCaseImpl
    @Inject
    constructor(
        private val launchpadRepository: LaunchpadRepository,
    ) : GetLaunchpadUseCase {
        override suspend fun invoke(id: String): Result<Launchpad> = launchpadRepository.getLaunchpad(id)
    }
