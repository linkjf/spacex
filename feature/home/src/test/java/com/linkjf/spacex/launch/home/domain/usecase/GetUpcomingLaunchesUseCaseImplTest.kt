package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launch
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
            // Given
            val expectedLaunches =
                listOf(
                    createSampleLaunch("launch1", "Launch 1", upcoming = true),
                    createSampleLaunch("launch2", "Launch 2", upcoming = true),
                )
            coEvery { launchRepository.getUpcomingLaunches() } returns Result.success(expectedLaunches)

            // When
            val result = getUpcomingLaunchesUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launches", expectedLaunches, result.getOrNull())
            coVerify { launchRepository.getUpcomingLaunches() }
        }

    @Test
    fun `invoke should return failure when repository returns failure`() =
        runTest {
            // Given
            val exception = IOException("Network error")
            coEvery { launchRepository.getUpcomingLaunches() } returns Result.failure(exception)

            // When
            val result = getUpcomingLaunchesUseCase()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchRepository.getUpcomingLaunches() }
        }

    @Test
    fun `invoke should handle empty list from repository`() =
        runTest {
            // Given
            val emptyLaunches = emptyList<Launch>()
            coEvery { launchRepository.getUpcomingLaunches() } returns Result.success(emptyLaunches)

            // When
            val result = getUpcomingLaunchesUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { launchRepository.getUpcomingLaunches() }
        }

    @Test
    fun `invoke should handle repository exception`() =
        runTest {
            // Given
            val exception = RuntimeException("Repository error")
            coEvery { launchRepository.getUpcomingLaunches() } throws exception

            // When & Then - Exception should be thrown since use case doesn't catch it
            try {
                getUpcomingLaunchesUseCase()
                assertTrue("Should have thrown exception", false)
            } catch (e: RuntimeException) {
                assertEquals("Should return the exception", exception, e)
            }
            coVerify { launchRepository.getUpcomingLaunches() }
        }

    @Test
    fun `invoke should be callable as function`() =
        runTest {
            // Given
            val expectedLaunches = listOf(createSampleLaunch("launch1", "Launch 1", upcoming = true))
            coEvery { launchRepository.getUpcomingLaunches() } returns Result.success(expectedLaunches)

            // When - Test that the use case can be called as a function
            val result = getUpcomingLaunchesUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launches", expectedLaunches, result.getOrNull())
            coVerify { launchRepository.getUpcomingLaunches() }
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
