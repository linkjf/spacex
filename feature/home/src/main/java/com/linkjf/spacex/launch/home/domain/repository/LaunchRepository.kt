package com.linkjf.spacex.launch.home.domain.repository

import com.linkjf.spacex.launch.home.domain.model.Launch

interface LaunchRepository {
    suspend fun getUpcomingLaunches(): Result<List<Launch>>

    suspend fun getPastLaunches(): Result<List<Launch>>
}
