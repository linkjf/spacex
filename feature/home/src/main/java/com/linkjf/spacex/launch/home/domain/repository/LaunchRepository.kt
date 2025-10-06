package com.linkjf.spacex.launch.home.domain.repository

import androidx.paging.PagingData
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import kotlinx.coroutines.flow.Flow

interface LaunchRepository {
    // New Paging 3 methods
    fun getUpcomingLaunches(): Flow<PagingData<LaunchListItem>>

    fun getPastLaunches(): Flow<PagingData<LaunchListItem>>
}
