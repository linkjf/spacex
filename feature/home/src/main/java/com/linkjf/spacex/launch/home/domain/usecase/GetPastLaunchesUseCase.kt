package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.PaginatedLaunches
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting past launches (pack)
 */
interface GetPastLaunchesUseCase {
    operator fun invoke(
        limit: Int = 20,
        offset: Int = 0,
    ): Flow<Result<PaginatedLaunches>>
}
