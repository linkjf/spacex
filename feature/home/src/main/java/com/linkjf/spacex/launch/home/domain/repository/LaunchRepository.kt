package com.linkjf.spacex.launch.home.domain.repository

import com.linkjf.spacex.launch.home.domain.model.PaginatedLaunches
import kotlinx.coroutines.flow.Flow

interface LaunchRepository {
    fun getUpcomingLaunches(
        limit: Int = 20,
        offset: Int = 0,
    ): Flow<Result<PaginatedLaunches>>

    fun getPastLaunches(
        limit: Int = 20,
        offset: Int = 0,
    ): Flow<Result<PaginatedLaunches>>
}
