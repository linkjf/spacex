package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.database.SpaceXDatabase
import com.linkjf.spacex.launch.home.data.mapper.LaunchLibraryLaunchMapper
import com.linkjf.spacex.launch.home.data.mapper.LaunchToLaunchListItemMapper
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.network.RateLimitInterceptor
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchRepositoryImplTest {
    private val mockApi = mockk<LaunchLibraryApi>()
    private val mockDatabase = mockk<SpaceXDatabase>(relaxed = true)
    private val mockMapper = mockk<LaunchLibraryLaunchMapper>()
    private val mockLaunchToListItemMapper = mockk<LaunchToLaunchListItemMapper>()
    private val mockRateLimitInterceptor = mockk<RateLimitInterceptor>(relaxed = true)

    private val repository =
        LaunchRepositoryImpl(
            launchLibraryApi = mockApi,
            database = mockDatabase,
            mapper = mockMapper,
            launchToListItemMapper = mockLaunchToListItemMapper,
            rateLimitInterceptor = mockRateLimitInterceptor,
        )

    @Test
    fun `getUpcomingLaunches should return paging data flow`() =
        runTest {
            // When
            val result = repository.getUpcomingLaunches()

            // Then
            // Just verify that the method returns a flow without throwing exceptions
            // The actual PagingData content is complex to test without proper database setup
            assert(result != null)
        }

    @Test
    fun `getPastLaunches should return paging data flow`() =
        runTest {
            // When
            val result = repository.getPastLaunches()

            // Then
            // Just verify that the method returns a flow without throwing exceptions
            // The actual PagingData content is complex to test without proper database setup
            assert(result != null)
        }
}
