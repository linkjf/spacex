package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.PaginatedLaunches
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
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
        override fun invoke(
            limit: Int,
            offset: Int,
        ): Flow<Result<PaginatedLaunches>> = launchRepository.getUpcomingLaunches(limit, offset)
    }
