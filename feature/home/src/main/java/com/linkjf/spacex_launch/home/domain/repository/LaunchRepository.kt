package com.linkjf.spacex_launch.home.domain.repository

import com.linkjf.spacex_launch.home.domain.model.Launch

interface LaunchRepository {
    suspend fun getUpcomingLaunches(): Result<List<Launch>>
}
