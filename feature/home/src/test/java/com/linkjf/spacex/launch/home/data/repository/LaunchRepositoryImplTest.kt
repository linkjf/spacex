package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.LaunchLibraryLaunchMapper
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLibraryLaunchDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLibraryResponseDto
import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.PaginatedLaunches
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class LaunchRepositoryImplTest {
    private lateinit var launchLibraryApi: LaunchLibraryApi
    private lateinit var launchRepository: LaunchRepositoryImpl

    @Before
    fun setUp() {
        launchLibraryApi = mockk()
        launchRepository = LaunchRepositoryImpl(launchLibraryApi)
        mockkObject(LaunchLibraryLaunchMapper)
    }

    @Test
    fun `getUpcomingLaunches should return success when API call succeeds`() =
        runTest {
            val launchDtos = emptyList<LaunchLibraryLaunchDto>()
            val apiResponse =
                LaunchLibraryResponseDto(
                    count = 2,
                    next = null,
                    previous = null,
                    results = launchDtos,
                )
            val expectedLaunches =
                listOf(
                    createSampleLaunch("launch1", "Launch 1", upcoming = true),
                    createSampleLaunch("launch2", "Launch 2", upcoming = true),
                )

            coEvery { launchLibraryApi.getUpcomingLaunches(20, 0) } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(launchDtos, true) } returns expectedLaunches

            val result = launchRepository.getUpcomingLaunches(20, 0)

            assertTrue("Result should be success", result.isSuccess)
            val paginatedResult = result.getOrNull()
            assertNotNull("Paginated result should not be null", paginatedResult)
            assertEquals("Should return mapped launches", expectedLaunches, paginatedResult?.launches)
            assertEquals("Should return correct count", 2, paginatedResult?.totalCount)
            assertFalse("Should not have more items", paginatedResult?.hasMore ?: true)
            coVerify { launchLibraryApi.getUpcomingLaunches(20, 0) }
        }

    @Test
    fun `getUpcomingLaunches should return failure when API call fails`() =
        runTest {
            val exception = IOException("Network error")
            coEvery { launchLibraryApi.getUpcomingLaunches(20, 0) } throws exception

            val result = launchRepository.getUpcomingLaunches(20, 0)

            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchLibraryApi.getUpcomingLaunches(20, 0) }
        }

    @Test
    fun `getPastLaunches should return success when API call succeeds`() =
        runTest {
            val launchDtos = emptyList<LaunchLibraryLaunchDto>()
            val apiResponse =
                LaunchLibraryResponseDto(
                    count = 2,
                    next = null,
                    previous = null,
                    results = launchDtos,
                )
            val expectedLaunches =
                listOf(
                    createSampleLaunch("launch1", "Launch 1", upcoming = false),
                    createSampleLaunch("launch2", "Launch 2", upcoming = false),
                )

            coEvery { launchLibraryApi.getPastLaunches(20, 0) } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(launchDtos, false) } returns expectedLaunches

            val result = launchRepository.getPastLaunches(20, 0)

            assertTrue("Result should be success", result.isSuccess)
            val paginatedResult = result.getOrNull()
            assertNotNull("Paginated result should not be null", paginatedResult)
            assertEquals("Should return mapped launches", expectedLaunches, paginatedResult?.launches)
            assertEquals("Should return correct count", 2, paginatedResult?.totalCount)
            assertFalse("Should not have more items", paginatedResult?.hasMore ?: true)
            coVerify { launchLibraryApi.getPastLaunches(20, 0) }
        }

    @Test
    fun `getPastLaunches should return failure when API call fails`() =
        runTest {
            val exception = IOException("Network error")
            coEvery { launchLibraryApi.getPastLaunches(20, 0) } throws exception

            val result = launchRepository.getPastLaunches(20, 0)

            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchLibraryApi.getPastLaunches(20, 0) }
        }

    @Test
    fun `getUpcomingLaunches should handle empty list from API`() =
        runTest {
            val emptyLaunchDtos = emptyList<LaunchLibraryLaunchDto>()
            val apiResponse =
                LaunchLibraryResponseDto(
                    count = 0,
                    next = null,
                    previous = null,
                    results = emptyLaunchDtos,
                )
            val emptyLaunches = emptyList<Launch>()

            coEvery { launchLibraryApi.getUpcomingLaunches(20, 0) } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(emptyLaunchDtos, true) } returns emptyLaunches

            val result = launchRepository.getUpcomingLaunches(20, 0)

            assertTrue("Result should be success", result.isSuccess)
            val paginatedResult = result.getOrNull()
            assertNotNull("Paginated result should not be null", paginatedResult)
            assertTrue("Should return empty list", paginatedResult?.launches?.isEmpty() == true)
            assertEquals("Should return correct count", 0, paginatedResult?.totalCount)
            assertFalse("Should not have more items", paginatedResult?.hasMore ?: true)
            coVerify { launchLibraryApi.getUpcomingLaunches(20, 0) }
        }

    @Test
    fun `getPastLaunches should handle empty list from API`() =
        runTest {
            val emptyLaunchDtos = emptyList<LaunchLibraryLaunchDto>()
            val apiResponse =
                LaunchLibraryResponseDto(
                    count = 0,
                    next = null,
                    previous = null,
                    results = emptyLaunchDtos,
                )
            val emptyLaunches = emptyList<Launch>()

            coEvery { launchLibraryApi.getPastLaunches(20, 0) } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(emptyLaunchDtos, false) } returns emptyLaunches

            val result = launchRepository.getPastLaunches(20, 0)

            assertTrue("Result should be success", result.isSuccess)
            val paginatedResult = result.getOrNull()
            assertNotNull("Paginated result should not be null", paginatedResult)
            assertTrue("Should return empty list", paginatedResult?.launches?.isEmpty() == true)
            assertEquals("Should return correct count", 0, paginatedResult?.totalCount)
            assertFalse("Should not have more items", paginatedResult?.hasMore ?: true)
            coVerify { launchLibraryApi.getPastLaunches(20, 0) }
        }

    @Test
    fun `getUpcomingLaunches should handle pagination with next page`() =
        runTest {
            val launchDtos = emptyList<LaunchLibraryLaunchDto>()
            val apiResponse =
                LaunchLibraryResponseDto(
                    count = 50,
                    next = "https://api.example.com/launches/upcoming/?limit=20&offset=20",
                    previous = null,
                    results = launchDtos,
                )
            val expectedLaunches = listOf(createSampleLaunch("launch1", "Launch 1", upcoming = true))

            coEvery { launchLibraryApi.getUpcomingLaunches(20, 0) } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(launchDtos, true) } returns expectedLaunches

            val result = launchRepository.getUpcomingLaunches(20, 0)

            assertTrue("Result should be success", result.isSuccess)
            val paginatedResult = result.getOrNull()
            assertNotNull("Paginated result should not be null", paginatedResult)
            assertEquals("Should return mapped launches", expectedLaunches, paginatedResult?.launches)
            assertEquals("Should return correct count", 50, paginatedResult?.totalCount)
            assertTrue("Should have more items", paginatedResult?.hasMore ?: false)
            coVerify { launchLibraryApi.getUpcomingLaunches(20, 0) }
        }

    private fun createSampleLaunch(
        id: String,
        name: String,
        upcoming: Boolean,
    ): Launch =
        Launch(
            id = id,
            name = name,
            dateUtc = "2023-01-01T00:00:00.000Z",
            rocketId = "rocket-id",
            launchpadId = "launchpad-id",
            links = null,
            details = null,
            success = null,
            upcoming = upcoming,
            flightNumber = null,
            staticFireDateUtc = null,
            tbd = null,
            net = null,
            window = null,
            rocket = null,
            launchpad = null,
            payloads = null,
            capsules = null,
            ships = null,
            crew = null,
            cores = null,
            fairings = null,
            autoUpdate = null,
            dateLocal = null,
            datePrecision = null,
            dateUnix = null,
        )
}
