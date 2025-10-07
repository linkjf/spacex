package com.linkjf.spacex.launch.home.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.linkjf.spacex.launch.database.SpaceXDatabase
import com.linkjf.spacex.launch.database.entity.LaunchEntity
import com.linkjf.spacex.launch.database.entity.LaunchesRemoteKeysEntity
import com.linkjf.spacex.launch.home.data.mapper.DatabaseMapper
import com.linkjf.spacex.launch.home.data.mapper.LaunchLibraryLaunchMapper
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.network.RateLimitInterceptor
import javax.inject.Inject
import retrofit2.HttpException as RetrofitHttpException

@OptIn(ExperimentalPagingApi::class)
class LaunchRemoteMediator
@Inject
constructor(
    private val database: SpaceXDatabase,
    private val api: LaunchLibraryApi,
    private val mapper: LaunchLibraryLaunchMapper,
    private val rateLimitInterceptor: RateLimitInterceptor,
    private val isUpcoming: Boolean,
) : RemoteMediator<Int, LaunchEntity>() {
    private val launchDao = database.launchDao()
    private val remoteKeysDao = database.launchRemoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val cachedCount = launchDao.getLaunchCountByType(isUpcoming)
        Log.d(
            "LaunchRemoteMediator",
            "Initialize: isUpcoming=$isUpcoming, cachedCount=$cachedCount",
        )

        return if (cachedCount == 0) {
            Log.d("LaunchRemoteMediator", "No cached data, will fetch in background")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            Log.d(
                "LaunchRemoteMediator",
                "Has cached data ($cachedCount items), showing local first"
            )
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LaunchEntity>,
    ): MediatorResult {
        val limit = state.config.pageSize
        val offset =
            when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> {
                    val first = state.firstItemOrNull() ?: return MediatorResult.Success(true)
                    remoteKeysDao.remoteKeysByLaunch(first.id, isUpcoming)?.prevOffset
                        ?: return MediatorResult.Success(true)
                }

                LoadType.APPEND -> {
                    val last = state.lastItemOrNull()
                    if (last == null) {
                        Log.d("LaunchRemoteMediator", "APPEND: No last item, ending pagination")
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    val remoteKey = remoteKeysDao.remoteKeysByLaunch(last.id, isUpcoming)
                    if (remoteKey == null) {
                        Log.e(
                            "LaunchRemoteMediator",
                            "APPEND: No remote key found for launch ${last.id}, isUpcoming=$isUpcoming"
                        )
                        return MediatorResult.Error(Exception("Remote key not found"))
                    }

                    val nextOffset = remoteKey.nextOffset
                    if (nextOffset == null) {
                        Log.d(
                            "LaunchRemoteMediator",
                            "APPEND: nextOffset is null, reached end of pagination"
                        )
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    Log.d(
                        "LaunchRemoteMediator",
                        "APPEND: nextOffset=$nextOffset for launch ${last.id}"
                    )
                    nextOffset
                }
            }

        Log.d(
            "LaunchRemoteMediator",
            "Load: isUpcoming=$isUpcoming, loadType=$loadType, offset=$offset, limit=$limit",
        )

        return try {
            val response =
                if (isUpcoming) {
                    api.getUpcomingLaunches(limit = limit, offset = offset)
                } else {
                    api.getPastLaunches(limit = limit, offset = offset)
                }

            val launches = mapper.mapToDomain(response.results, isUpcoming = isUpcoming)
            val endOfList = response.next == null

            Log.d(
                "LaunchRemoteMediator",
                "API Response: results=${response.results.size}, next=${response.next}, endOfList=$endOfList",
            )

            if (launches.isEmpty()) {
                Log.d("LaunchRemoteMediator", "Empty response, ending pagination")
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearByType(isUpcoming)
                    launchDao.deleteByType(isUpcoming)
                }

                val entities =
                    launches.map { launch ->
                        DatabaseMapper.run { launch.toEntity() }
                    }

                val prevOffset = if (offset == 0) null else maxOf(0, offset - limit)
                val nextOffset = if (endOfList) null else offset + limit

                launchDao.insertLaunches(entities)
                remoteKeysDao.upsertAll(
                    entities.map { entity ->
                        LaunchesRemoteKeysEntity(
                            launchId = entity.id,
                            isUpcoming = isUpcoming,
                            prevOffset = prevOffset,
                            nextOffset = nextOffset,
                        )
                    },
                )
            }

            Log.d(
                "LaunchRemoteMediator",
                "Success: inserted ${launches.size} launches, endOfPaginationReached=$endOfList",
            )
            MediatorResult.Success(endOfPaginationReached = endOfList)
        } catch (e: RetrofitHttpException) {
            if (e.code() == 429) {
                val rateLimitInfo = rateLimitInterceptor.getRateLimitInfo()
                Log.e(
                    "LaunchRemoteMediator",
                    "Rate limit exceeded (429), remaining: ${rateLimitInfo?.remainingSeconds}s",
                )
            } else {
                Log.e("LaunchRemoteMediator", "HTTP error ${e.code()}: ${e.message()}")
            }
            MediatorResult.Error(e)
        } catch (t: Throwable) {
            Log.e("LaunchRemoteMediator", "Error loading data", t)
            MediatorResult.Error(t)
        }
    }
}
