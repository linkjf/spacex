package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.LaunchpadMapper
import com.linkjf.spacex.launch.home.data.remote.SpaceXApi
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchpadDto
import com.linkjf.spacex.launch.home.domain.model.Launchpad
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

class LaunchpadRepositoryImplTest {
    private lateinit var spaceXApi: SpaceXApi
    private lateinit var launchpadRepository: LaunchpadRepositoryImpl

    @Before
    fun setUp() {
        spaceXApi = mockk()
        launchpadRepository = LaunchpadRepositoryImpl(spaceXApi)
        mockkObject(LaunchpadMapper)
    }

    @Test
    fun `getLaunchpads should return success when API call succeeds`() =
        runTest {
            // Given
            val launchpadDtos =
                listOf(
                    createSampleLaunchpadDto("ksc_lc_39a", "KSC LC 39A"),
                    createSampleLaunchpadDto("slc_40", "SLC 40"),
                )
            val expectedLaunchpads =
                listOf(
                    createSampleLaunchpad("ksc_lc_39a", "KSC LC 39A"),
                    createSampleLaunchpad("slc_40", "SLC 40"),
                )

            coEvery { spaceXApi.getLaunchpads() } returns launchpadDtos
            every { LaunchpadMapper.mapToDomain(launchpadDtos) } returns expectedLaunchpads

            // When
            val result = launchpadRepository.getLaunchpads()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped launchpads", expectedLaunchpads, result.getOrNull())
            coVerify { spaceXApi.getLaunchpads() }
        }

    @Test
    fun `getLaunchpads should return failure when API call fails`() =
        runTest {
            // Given
            val exception = IOException("Network error")
            coEvery { spaceXApi.getLaunchpads() } throws exception

            // When
            val result = launchpadRepository.getLaunchpads()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getLaunchpads() }
        }

    @Test
    fun `getLaunchpads should return failure when mapper fails`() =
        runTest {
            // Given
            val launchpadDtos = listOf(createSampleLaunchpadDto("ksc_lc_39a", "KSC LC 39A"))
            val exception = RuntimeException("Mapping error")

            coEvery { spaceXApi.getLaunchpads() } returns launchpadDtos
            every { LaunchpadMapper.mapToDomain(launchpadDtos) } throws exception

            // When
            val result = launchpadRepository.getLaunchpads()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getLaunchpads() }
        }

    @Test
    fun `getLaunchpad should return success when API call succeeds`() =
        runTest {
            // Given
            val launchpadId = "ksc_lc_39a"
            val launchpadDto = createSampleLaunchpadDto(launchpadId, "KSC LC 39A")
            val expectedLaunchpad = createSampleLaunchpad(launchpadId, "KSC LC 39A")

            coEvery { spaceXApi.getLaunchpad(launchpadId) } returns launchpadDto
            every { LaunchpadMapper.mapToDomain(launchpadDto) } returns expectedLaunchpad

            // When
            val result = launchpadRepository.getLaunchpad(launchpadId)

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped launchpad", expectedLaunchpad, result.getOrNull())
            coVerify { spaceXApi.getLaunchpad(launchpadId) }
        }

    @Test
    fun `getLaunchpad should return failure when API call fails`() =
        runTest {
            // Given
            val launchpadId = "ksc_lc_39a"
            val exception = IOException("Network error")
            coEvery { spaceXApi.getLaunchpad(launchpadId) } throws exception

            // When
            val result = launchpadRepository.getLaunchpad(launchpadId)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getLaunchpad(launchpadId) }
        }

    @Test
    fun `getLaunchpad should return failure when mapper fails`() =
        runTest {
            // Given
            val launchpadId = "ksc_lc_39a"
            val launchpadDto = createSampleLaunchpadDto(launchpadId, "KSC LC 39A")
            val exception = RuntimeException("Mapping error")

            coEvery { spaceXApi.getLaunchpad(launchpadId) } returns launchpadDto
            every { LaunchpadMapper.mapToDomain(launchpadDto) } throws exception

            // When
            val result = launchpadRepository.getLaunchpad(launchpadId)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getLaunchpad(launchpadId) }
        }

    @Test
    fun `getLaunchpads should handle empty list from API`() =
        runTest {
            // Given
            val emptyLaunchpadDtos = emptyList<LaunchpadDto>()
            val emptyLaunchpads = emptyList<Launchpad>()

            coEvery { spaceXApi.getLaunchpads() } returns emptyLaunchpadDtos
            every { LaunchpadMapper.mapToDomain(emptyLaunchpadDtos) } returns emptyLaunchpads

            // When
            val result = launchpadRepository.getLaunchpads()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { spaceXApi.getLaunchpads() }
        }

    @Test
    fun `getLaunchpad should handle different launchpad IDs`() =
        runTest {
            // Given
            val launchpadIds = listOf("ksc_lc_39a", "slc_40", "vafb_slc_4e")

            launchpadIds.forEach { launchpadId ->
                val launchpadDto = createSampleLaunchpadDto(launchpadId, "Launchpad $launchpadId")
                val expectedLaunchpad = createSampleLaunchpad(launchpadId, "Launchpad $launchpadId")

                coEvery { spaceXApi.getLaunchpad(launchpadId) } returns launchpadDto
                every { LaunchpadMapper.mapToDomain(launchpadDto) } returns expectedLaunchpad

                // When
                val result = launchpadRepository.getLaunchpad(launchpadId)

                // Then
                assertTrue("Result should be success for $launchpadId", result.isSuccess)
                assertEquals("Should return correct launchpad for $launchpadId", expectedLaunchpad, result.getOrNull())
                coVerify { spaceXApi.getLaunchpad(launchpadId) }
            }
        }

    @Test
    fun `getLaunchpad should handle special characters in ID`() =
        runTest {
            // Given
            val launchpadId = "ksc-lc-39a"
            val launchpadDto = createSampleLaunchpadDto(launchpadId, "KSC LC 39A")
            val expectedLaunchpad = createSampleLaunchpad(launchpadId, "KSC LC 39A")

            coEvery { spaceXApi.getLaunchpad(launchpadId) } returns launchpadDto
            every { LaunchpadMapper.mapToDomain(launchpadDto) } returns expectedLaunchpad

            // When
            val result = launchpadRepository.getLaunchpad(launchpadId)

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped launchpad", expectedLaunchpad, result.getOrNull())
            coVerify { spaceXApi.getLaunchpad(launchpadId) }
        }

    private fun createSampleLaunchpadDto(
        id: String,
        name: String,
    ): LaunchpadDto =
        LaunchpadDto(
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
