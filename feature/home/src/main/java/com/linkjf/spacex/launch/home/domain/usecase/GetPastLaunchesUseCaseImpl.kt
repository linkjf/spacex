package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.PaginatedLaunches
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of GetPastLaunchesUseCase
 */
@Singleton
class GetPastLaunchesUseCaseImpl
    @Inject
    constructor(
        private val launchRepository: LaunchRepository,
    ) : GetPastLaunchesUseCase {
        override fun invoke(
            limit: Int,
            offset: Int,
        ): Flow<Result<PaginatedLaunches>> = launchRepository.getPastLaunches(limit, offset)
    }
