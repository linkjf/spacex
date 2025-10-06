package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.local.LocalDataSource
import com.linkjf.spacex.launch.home.data.mapper.LaunchLibraryLaunchMapper
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.home.domain.model.PaginatedLaunches
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import com.linkjf.spacex.launch.network.exceptions.HttpException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException as RetrofitHttpException

@Singleton
class LaunchRepositoryImpl
    @Inject
    constructor(
        private val launchLibraryApi: LaunchLibraryApi,
        private val localDataSource: LocalDataSource,
    ) : LaunchRepository {
        override fun getUpcomingLaunches(
            limit: Int,
            offset: Int,
        ): Flow<Result<PaginatedLaunches>> =
            flow {
                // Offline-first strategy: Return cached data immediately, then update from network
                try {
                    // First, emit cached data if available
                    val cachedLaunches = localDataSource.getUpcomingLaunchesPaginated(limit, offset).first()
                    if (cachedLaunches.isNotEmpty()) {
                        val cachedCount = localDataSource.getUpcomingLaunchCount()
                        emit(
                            Result.success(
                                PaginatedLaunches(
                                    launches = cachedLaunches,
                                    totalCount = cachedCount,
                                    hasMore = cachedLaunches.size >= limit,
                                ),
                            ),
                        )
                    }

                    // Then try to fetch fresh data from network
                    val response = launchLibraryApi.getUpcomingLaunches(limit, offset)
                    val launches = LaunchLibraryLaunchMapper.mapToDomain(response.results, isUpcoming = true)
                    val hasMore = response.next != null

                    // Save to local storage
                    localDataSource.insertLaunches(launches)

                    val paginatedLaunches =
                        PaginatedLaunches(
                            launches = launches,
                            totalCount = response.count,
                            hasMore = hasMore,
                        )

                    // Emit fresh data
                    emit(Result.success(paginatedLaunches))
                } catch (e: RetrofitHttpException) {
                    // If network fails, try to return cached data
                    val cachedLaunches = localDataSource.getUpcomingLaunchesPaginated(limit, offset).first()
                    if (cachedLaunches.isNotEmpty()) {
                        val cachedCount = localDataSource.getUpcomingLaunchCount()
                        emit(
                            Result.success(
                                PaginatedLaunches(
                                    launches = cachedLaunches,
                                    totalCount = cachedCount,
                                    hasMore = cachedLaunches.size >= limit,
                                ),
                            ),
                        )
                    } else {
                        emit(Result.failure(handleHttpException(e)))
                    }
                } catch (e: IOException) {
                    // Network error - try cached data
                    val cachedLaunches = localDataSource.getUpcomingLaunchesPaginated(limit, offset).first()
                    if (cachedLaunches.isNotEmpty()) {
                        val cachedCount = localDataSource.getUpcomingLaunchCount()
                        emit(
                            Result.success(
                                PaginatedLaunches(
                                    launches = cachedLaunches,
                                    totalCount = cachedCount,
                                    hasMore = cachedLaunches.size >= limit,
                                ),
                            ),
                        )
                    } else {
                        emit(Result.failure(HttpException.NetworkError(cause = e)))
                    }
                } catch (e: Exception) {
                    emit(Result.failure(e))
                }
            }

        override fun getPastLaunches(
            limit: Int,
            offset: Int,
        ): Flow<Result<PaginatedLaunches>> =
            flow {
                // Offline-first strategy: Return cached data immediately, then update from network
                try {
                    // First, emit cached data if available
                    val cachedLaunches = localDataSource.getPastLaunchesPaginated(limit, offset).first()
                    if (cachedLaunches.isNotEmpty()) {
                        val cachedCount = localDataSource.getPastLaunchCount()
                        emit(
                            Result.success(
                                PaginatedLaunches(
                                    launches = cachedLaunches,
                                    totalCount = cachedCount,
                                    hasMore = cachedLaunches.size >= limit,
                                ),
                            ),
                        )
                    }

                    // Then try to fetch fresh data from network
                    val response = launchLibraryApi.getPastLaunches(limit, offset)
                    val launches = LaunchLibraryLaunchMapper.mapToDomain(response.results, isUpcoming = false)
                    val hasMore = response.next != null

                    // Save to local storage
                    localDataSource.insertLaunches(launches)

                    val paginatedLaunches =
                        PaginatedLaunches(
                            launches = launches,
                            totalCount = response.count,
                            hasMore = hasMore,
                        )

                    // Emit fresh data
                    emit(Result.success(paginatedLaunches))
                } catch (e: RetrofitHttpException) {
                    // If network fails, try to return cached data
                    val cachedLaunches = localDataSource.getPastLaunchesPaginated(limit, offset).first()
                    if (cachedLaunches.isNotEmpty()) {
                        val cachedCount = localDataSource.getPastLaunchCount()
                        emit(
                            Result.success(
                                PaginatedLaunches(
                                    launches = cachedLaunches,
                                    totalCount = cachedCount,
                                    hasMore = cachedLaunches.size >= limit,
                                ),
                            ),
                        )
                    } else {
                        emit(Result.failure(handleHttpException(e)))
                    }
                } catch (e: IOException) {
                    // Network error - try cached data
                    val cachedLaunches = localDataSource.getPastLaunchesPaginated(limit, offset).first()
                    if (cachedLaunches.isNotEmpty()) {
                        val cachedCount = localDataSource.getPastLaunchCount()
                        emit(
                            Result.success(
                                PaginatedLaunches(
                                    launches = cachedLaunches,
                                    totalCount = cachedCount,
                                    hasMore = cachedLaunches.size >= limit,
                                ),
                            ),
                        )
                    } else {
                        emit(Result.failure(HttpException.NetworkError(cause = e)))
                    }
                } catch (e: Exception) {
                    emit(Result.failure(e))
                }
            }
    }

private fun handleHttpException(e: RetrofitHttpException): Exception =
    when (e.code()) {
        429 -> {
            // Try to extract Retry-After header
            val retryAfter =
                e
                    .response()
                    ?.headers()
                    ?.get("Retry-After")
                    ?.toIntOrNull()
            HttpException.TooManyRequests(retryAfterSeconds = retryAfter)
        }
        else ->
            HttpException.HttpError(
                statusCode = e.code(),
                message = e.message() ?: "HTTP ${e.code()} error",
            )
    }
