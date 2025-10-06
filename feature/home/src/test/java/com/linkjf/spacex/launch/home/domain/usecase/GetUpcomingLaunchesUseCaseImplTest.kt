package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.PaginatedLaunches
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetUpcomingLaunchesUseCaseImplTest {
    private lateinit var launchRepository: LaunchRepository
    private lateinit var getUpcomingLaunchesUseCase: GetUpcomingLaunchesUseCaseImpl

    @Before
    fun setUp() {
        launchRepository = mockk()
        getUpcomingLaunchesUseCase = GetUpcomingLaunchesUseCaseImpl(launchRepository)
    }

    @Test
    fun `invoke should return success when repository returns success`() =
        runTest {
            val expectedLaunches =
                listOf(
                    createSampleLaunch("launch1", "Launch 1", upcoming = true),
                    createSampleLaunch("launch2", "Launch 2", upcoming = true),
                )
            val paginatedLaunches = PaginatedLaunches(expectedLaunches, 2, false)
            coEvery { launchRepository.getUpcomingLaunches(20, 0) } returns Result.success(paginatedLaunches)

            val result = getUpcomingLaunchesUseCase(20, 0)

            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launches", expectedLaunches, result.getOrNull()?.launches)
            coVerify { launchRepository.getUpcomingLaunches(20, 0) }
        }

    @Test
    fun `invoke should return failure when repository returns failure`() =
        runTest {
            val exception = IOException("Network error")
            coEvery { launchRepository.getUpcomingLaunches(20, 0) } returns Result.failure(exception)

            val result = getUpcomingLaunchesUseCase(20, 0)

            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchRepository.getUpcomingLaunches(20, 0) }
        }

    @Test
    fun `invoke should handle empty list from repository`() =
        runTest {
            val emptyLaunches = emptyList<Launch>()
            val paginatedLaunches = PaginatedLaunches(emptyLaunches, 0, false)
            coEvery { launchRepository.getUpcomingLaunches(20, 0) } returns Result.success(paginatedLaunches)

            val result = getUpcomingLaunchesUseCase(20, 0)

            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.launches?.isEmpty() == true)
            coVerify { launchRepository.getUpcomingLaunches(20, 0) }
        }

    @Test
    fun `invoke should handle repository exception`() =
        runTest {
            val exception = RuntimeException("Repository error")
            coEvery { launchRepository.getUpcomingLaunches(20, 0) } throws exception

            try {
                getUpcomingLaunchesUseCase(20, 0)
                assertTrue("Should have thrown exception", false)
            } catch (e: RuntimeException) {
                assertEquals("Should return the exception", exception, e)
            }
            coVerify { launchRepository.getUpcomingLaunches(20, 0) }
        }

    @Test
    fun `invoke should be callable as function`() =
        runTest {
            val expectedLaunches = listOf(createSampleLaunch("launch1", "Launch 1", upcoming = true))
            val paginatedLaunches = PaginatedLaunches(expectedLaunches, 1, false)
            coEvery { launchRepository.getUpcomingLaunches(20, 0) } returns Result.success(paginatedLaunches)

            val result = getUpcomingLaunchesUseCase(20, 0)

            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launches", expectedLaunches, result.getOrNull()?.launches)
            coVerify { launchRepository.getUpcomingLaunches(20, 0) }
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
