package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Rocket

/**
 * Use case for getting a specific rocket by ID
 */
interface GetRocketUseCase {
    suspend operator fun invoke(id: String): Result<Rocket>
}
