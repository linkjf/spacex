package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.repository.LaunchpadRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class GetLaunchpadUseCaseImplTest {
    private lateinit var launchpadRepository: LaunchpadRepository
    private lateinit var getLaunchpadUseCase: GetLaunchpadUseCaseImpl

    @Before
    fun setUp() {
        launchpadRepository = mockk()
        getLaunchpadUseCase = GetLaunchpadUseCaseImpl(launchpadRepository)
    }

    @Test
    fun `invoke should return success when repository returns success`() =
        runTest {
            // Given
            val launchpadId = "ksc_lc_39a"
            val expectedLaunchpad = createSampleLaunchpad(launchpadId, "KSC LC 39A")
            coEvery { launchpadRepository.getLaunchpad(launchpadId) } returns Result.success(expectedLaunchpad)

            // When
            val result = getLaunchpadUseCase(launchpadId)

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launchpad", expectedLaunchpad, result.getOrNull())
            coVerify { launchpadRepository.getLaunchpad(launchpadId) }
        }

    @Test
    fun `invoke should return failure when repository returns failure`() =
        runTest {
            // Given
            val launchpadId = "ksc_lc_39a"
            val exception = IOException("Network error")
            coEvery { launchpadRepository.getLaunchpad(launchpadId) } returns Result.failure(exception)

            // When
            val result = getLaunchpadUseCase(launchpadId)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchpadRepository.getLaunchpad(launchpadId) }
        }

    @Test
    fun `invoke should handle repository exception`() =
        runTest {
            // Given
            val launchpadId = "ksc_lc_39a"
            val exception = RuntimeException("Repository error")
            coEvery { launchpadRepository.getLaunchpad(launchpadId) } throws exception

            // When & Then - Exception should be thrown since use case doesn't catch it
            try {
                getLaunchpadUseCase(launchpadId)
                assertTrue("Should have thrown exception", false)
            } catch (e: RuntimeException) {
                assertEquals("Should return the exception", exception, e)
            }
            coVerify { launchpadRepository.getLaunchpad(launchpadId) }
        }

    @Test
    fun `invoke should handle different launchpad IDs`() =
        runTest {
            // Given
            val launchpadIds = listOf("ksc_lc_39a", "slc_40", "vafb_slc_4e")

            launchpadIds.forEach { launchpadId ->
                val expectedLaunchpad = createSampleLaunchpad(launchpadId, "Launchpad $launchpadId")
                coEvery { launchpadRepository.getLaunchpad(launchpadId) } returns Result.success(expectedLaunchpad)

                // When
                val result = getLaunchpadUseCase(launchpadId)

                // Then
                assertTrue("Result should be success for $launchpadId", result.isSuccess)
                assertEquals("Should return correct launchpad for $launchpadId", expectedLaunchpad, result.getOrNull())
                coVerify { launchpadRepository.getLaunchpad(launchpadId) }
            }
        }

    @Test
    fun `invoke should handle special characters in ID`() =
        runTest {
            // Given
            val launchpadId = "ksc-lc-39a"
            val expectedLaunchpad = createSampleLaunchpad(launchpadId, "KSC LC 39A")
            coEvery { launchpadRepository.getLaunchpad(launchpadId) } returns Result.success(expectedLaunchpad)

            // When
            val result = getLaunchpadUseCase(launchpadId)

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launchpad", expectedLaunchpad, result.getOrNull())
            coVerify { launchpadRepository.getLaunchpad(launchpadId) }
        }

    @Test
    fun `invoke should be callable as function`() =
        runTest {
            // Given
            val launchpadId = "ksc_lc_39a"
            val expectedLaunchpad = createSampleLaunchpad(launchpadId, "KSC LC 39A")
            coEvery { launchpadRepository.getLaunchpad(launchpadId) } returns Result.success(expectedLaunchpad)

            // When - Test that the use case can be called as a function
            val result = getLaunchpadUseCase(launchpadId)

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected launchpad", expectedLaunchpad, result.getOrNull())
            coVerify { launchpadRepository.getLaunchpad(launchpadId) }
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
