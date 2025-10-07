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
import com.linkjf.spacex.launch.network.RateLimitInterceptor
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
    private val rateLimitInterceptor: RateLimitInterceptor,
) : LaunchRepository {
    private val upcomingLaunchesPager by lazy {
        createPager(isUpcoming = true)
    }

    private val pastLaunchesPager by lazy {
        createPager(isUpcoming = false)
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 5
        private const val INITIAL_LOAD_SIZE = 20
        private const val ENABLE_PLACEHOLDERS = false
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun createPager(isUpcoming: Boolean): Flow<PagingData<LaunchListItem>> =
        Pager(
            config =
                PagingConfig(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = PREFETCH_DISTANCE,
                    enablePlaceholders = ENABLE_PLACEHOLDERS,
                    initialLoadSize = INITIAL_LOAD_SIZE,
                ),
            remoteMediator =
                LaunchRemoteMediator(
                    database = database,
                    api = launchLibraryApi,
                    mapper = mapper,
                    rateLimitInterceptor = rateLimitInterceptor,
                    isUpcoming = isUpcoming,
                ),
            pagingSourceFactory = {
                database.launchDao().pagingSource(isUpcoming = isUpcoming)
            },
        ).flow.map { pagingData ->
            pagingData.map { launchEntity ->
                val launch = DatabaseMapper.run { launchEntity.toDomain() }
                launchToListItemMapper.mapToLaunchListItem(launch)
            }
        }

    override fun getUpcomingLaunches(): Flow<PagingData<LaunchListItem>> = upcomingLaunchesPager

    override fun getPastLaunches(): Flow<PagingData<LaunchListItem>> = pastLaunchesPager
}
