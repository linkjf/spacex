package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launch

/**
 * Use case for getting past launches (pack)
 */
interface GetPastLaunchesUseCase {
    suspend operator fun invoke(): Result<List<Launch>>
}
