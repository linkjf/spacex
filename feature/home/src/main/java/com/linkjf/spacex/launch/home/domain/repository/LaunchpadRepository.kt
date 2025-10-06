package com.linkjf.spacex.launch.home.domain.repository

import com.linkjf.spacex.launch.home.domain.model.Launchpad

interface LaunchpadRepository {
    suspend fun getLaunchpads(): Result<List<Launchpad>>

    suspend fun getLaunchpad(id: String): Result<Launchpad>
}
