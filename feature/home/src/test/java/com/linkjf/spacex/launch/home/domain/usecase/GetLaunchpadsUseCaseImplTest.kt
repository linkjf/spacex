package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.repository.LaunchpadRepository
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

class GetLaunchpadsUseCaseImplTest {
    private lateinit var launchpadRepository: LaunchpadRepository
    private lateinit var getLaunchpadsUseCase: GetLaunchpadsUseCaseImpl

    @Before
    fun setUp() {
        launchpadRepository = mockk()
        getLaunchpadsUseCase = GetLaunchpadsUseCaseImpl(launchpadRepository)
    }

    @Test
    fun `invoke should return success when repository returns success`() =
        runTest {
            // Given
            val expectedLaunchpads =
                listOf(
                    createSampleLaunchpad("ksc_lc_39a", "KSC LC 39A"),
                    createSampleLaunchpad("slc_40", "SLC 40"),
                )
            coEvery { launchpadRepository.getLaunchpads() } returns Result.success(expectedLaunchpads)

            // When
            val result = getLaunchpadsUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launchpads", expectedLaunchpads, result.getOrNull())
            coVerify { launchpadRepository.getLaunchpads() }
        }

    @Test
    fun `invoke should return failure when repository returns failure`() =
        runTest {
            // Given
            val exception = IOException("Network error")
            coEvery { launchpadRepository.getLaunchpads() } returns Result.failure(exception)

            // When
            val result = getLaunchpadsUseCase()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchpadRepository.getLaunchpads() }
        }

    @Test
    fun `invoke should handle empty list from repository`() =
        runTest {
            // Given
            val emptyLaunchpads = emptyList<Launchpad>()
            coEvery { launchpadRepository.getLaunchpads() } returns Result.success(emptyLaunchpads)

            // When
            val result = getLaunchpadsUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { launchpadRepository.getLaunchpads() }
        }

    @Test
    fun `invoke should handle repository exception`() =
        runTest {
            // Given
            val exception = RuntimeException("Repository error")
            coEvery { launchpadRepository.getLaunchpads() } throws exception

            // When & Then - Exception should be thrown since use case doesn't catch it
            try {
                getLaunchpadsUseCase()
                assertTrue("Should have thrown exception", false)
            } catch (e: RuntimeException) {
                assertEquals("Should return the exception", exception, e)
            }
            coVerify { launchpadRepository.getLaunchpads() }
        }

    @Test
    fun `invoke should be callable as function`() =
        runTest {
            // Given
            val expectedLaunchpads = listOf(createSampleLaunchpad("ksc_lc_39a", "KSC LC 39A"))
            coEvery { launchpadRepository.getLaunchpads() } returns Result.success(expectedLaunchpads)

            // When - Test that the use case can be called as a function
            val result = getLaunchpadsUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launchpads", expectedLaunchpads, result.getOrNull())
            coVerify { launchpadRepository.getLaunchpads() }
        }

    private fun createSampleLaunchpad(
        id: String,
        name: String,
    ): Launchpad =
        Launchpad(
            id = id,
            name = name,
            fullName = "Kennedy Space Center Historic Launch Complex 39A",
            status = "active",
            locality = "Cape Canaveral",
            region = "Florida",
            timezone = "America/New_York",
            latitude = 28.6080585,
            longitude = -80.6039558,
            launchAttempts = 127,
            launchSuccesses = 125,
            rockets = listOf("falcon9", "falcon-heavy"),
            launches = listOf("launch1", "launch2"),
            details = "SpaceX historic launch site that was used for the Apollo and Space Shuttle programs",
        )
}
