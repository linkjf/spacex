package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launchpad

/**
 * Use case for getting all launchpads
 */
interface GetLaunchpadsUseCase {
    suspend operator fun invoke(): Result<List<Launchpad>>
}
