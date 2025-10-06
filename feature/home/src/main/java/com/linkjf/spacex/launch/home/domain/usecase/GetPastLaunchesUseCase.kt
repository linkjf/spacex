package com.linkjf.spacex.launch.home.domain.usecase

import androidx.paging.PagingData
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import kotlinx.coroutines.flow.Flow

interface GetPastLaunchesUseCase {
    operator fun invoke(): Flow<PagingData<LaunchListItem>>
}
