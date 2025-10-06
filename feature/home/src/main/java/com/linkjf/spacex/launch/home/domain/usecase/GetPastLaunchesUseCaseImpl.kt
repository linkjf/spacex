package com.linkjf.spacex.launch.home.domain.usecase

import androidx.paging.PagingData
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPastLaunchesUseCaseImpl
    @Inject
    constructor(
        private val launchRepository: LaunchRepository,
    ) : GetPastLaunchesUseCase {
        override fun invoke(): Flow<PagingData<LaunchListItem>> = launchRepository.getPastLaunches()
    }
