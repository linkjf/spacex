package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launchpad

/**
 * Use case for getting a specific launchpad by ID
 */
interface GetLaunchpadUseCase {
    suspend operator fun invoke(id: String): Result<Launchpad>
}
