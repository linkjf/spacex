package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Rocket

/**
 * Use case for getting all rockets
 */
interface GetRocketsUseCase {
    suspend operator fun invoke(): Result<List<Rocket>>
}
