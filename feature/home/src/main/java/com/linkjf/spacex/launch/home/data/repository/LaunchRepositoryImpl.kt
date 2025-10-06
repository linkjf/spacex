package com.linkjf.spacex.launch.home.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.linkjf.spacex.launch.database.SpaceXDatabase
import com.linkjf.spacex.launch.designsystem.components.LaunchListItem
import com.linkjf.spacex.launch.home.data.mapper.DatabaseMapper
import com.linkjf.spacex.launch.home.data.mapper.LaunchLibraryLaunchMapper
import com.linkjf.spacex.launch.home.data.mapper.LaunchToLaunchListItemMapper
import com.linkjf.spacex.launch.home.data.paging.LaunchRemoteMediator
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchRepositoryImpl
    @Inject
    constructor(
        private val launchLibraryApi: LaunchLibraryApi,
        private val database: SpaceXDatabase,
        private val mapper: LaunchLibraryLaunchMapper,
        private val launchToListItemMapper: LaunchToLaunchListItemMapper,
    ) : LaunchRepository {
        @OptIn(ExperimentalPagingApi::class)
        override fun getUpcomingLaunches(): Flow<PagingData<LaunchListItem>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = 20,
                        prefetchDistance = 2,
                        enablePlaceholders = false,
                    ),
                remoteMediator =
                    LaunchRemoteMediator(
                        database,
                        launchLibraryApi,
                        mapper,
                        isUpcoming = true,
                    ),
                pagingSourceFactory = {
                    database.launchDao().pagingSource(isUpcoming = true)
                },
            ).flow.map { pagingData ->
                pagingData.map { launchEntity ->
                    val launch = DatabaseMapper.run { launchEntity.toDomain() }
                    launchToListItemMapper.mapToLaunchListItem(launch)
                }
            }

        @OptIn(ExperimentalPagingApi::class)
        override fun getPastLaunches(): Flow<PagingData<LaunchListItem>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = 20,
                        prefetchDistance = 2,
                        enablePlaceholders = false,
                    ),
                remoteMediator =
                    LaunchRemoteMediator(
                        database,
                        launchLibraryApi,
                        mapper,
                        isUpcoming = false,
                    ),
                pagingSourceFactory = {
                    database.launchDao().pagingSource(isUpcoming = false)
                },
            ).flow.map { pagingData ->
                pagingData.map { launchEntity ->
                    val launch = DatabaseMapper.run { launchEntity.toDomain() }
                    launchToListItemMapper.mapToLaunchListItem(launch)
                }
            }
    }
