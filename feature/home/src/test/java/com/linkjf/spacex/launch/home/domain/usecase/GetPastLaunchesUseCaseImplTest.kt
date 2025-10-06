package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetPastLaunchesUseCaseImplTest {
    private lateinit var launchRepository: LaunchRepository
    private lateinit var getPastLaunchesUseCase: GetPastLaunchesUseCaseImpl

    @Before
    fun setUp() {
        launchRepository = mockk()
        getPastLaunchesUseCase = GetPastLaunchesUseCaseImpl(launchRepository)
    }

    @Test
    fun `invoke should return success when repository returns success`() =
        runTest {
            val expectedLaunches =
                listOf(
                    createSampleLaunch("launch1", "Launch 1", upcoming = false),
                    createSampleLaunch("launch2", "Launch 2", upcoming = false),
                )
            coEvery { launchRepository.getPastLaunches() } returns Result.success(expectedLaunches)

            val result = getPastLaunchesUseCase()

            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launches", expectedLaunches, result.getOrNull())
            coVerify { launchRepository.getPastLaunches() }
        }

    @Test
    fun `invoke should return failure when repository returns failure`() =
        runTest {
            val exception = IOException("Network error")
            coEvery { launchRepository.getPastLaunches() } returns Result.failure(exception)

            val result = getPastLaunchesUseCase()

            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchRepository.getPastLaunches() }
        }

    @Test
    fun `invoke should handle empty list from repository`() =
        runTest {
            val emptyLaunches = emptyList<Launch>()
            coEvery { launchRepository.getPastLaunches() } returns Result.success(emptyLaunches)

            val result = getPastLaunchesUseCase()

            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { launchRepository.getPastLaunches() }
        }

    @Test
    fun `invoke should handle repository exception`() =
        runTest {
            val exception = RuntimeException("Repository error")
            coEvery { launchRepository.getPastLaunches() } throws exception

            try {
                getPastLaunchesUseCase()
                assertTrue("Should have thrown exception", false)
            } catch (e: RuntimeException) {
                assertEquals("Should return the exception", exception, e)
            }
            coVerify { launchRepository.getPastLaunches() }
        }

    @Test
    fun `invoke should be callable as function`() =
        runTest {
            val expectedLaunches = listOf(createSampleLaunch("launch1", "Launch 1", upcoming = false))
            coEvery { launchRepository.getPastLaunches() } returns Result.success(expectedLaunches)

            val result = getPastLaunchesUseCase()

            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launches", expectedLaunches, result.getOrNull())
            coVerify { launchRepository.getPastLaunches() }
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
