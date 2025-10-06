package com.linkjf.spacex.launch.home.domain.repository

import com.linkjf.spacex.launch.home.domain.model.Rocket

interface RocketRepository {
    suspend fun getRockets(): Result<List<Rocket>>

    suspend fun getRocket(id: String): Result<Rocket>
}
