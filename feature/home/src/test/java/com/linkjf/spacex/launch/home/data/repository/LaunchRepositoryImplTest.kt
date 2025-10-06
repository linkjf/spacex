package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.LaunchMapper
import com.linkjf.spacex.launch.home.data.remote.SpaceXApi
import com.linkjf.spacex.launch.home.data.remote.dto.*
import com.linkjf.spacex.launch.home.domain.model.Launch
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
    private lateinit var spaceXApi: SpaceXApi
    private lateinit var launchRepository: LaunchRepositoryImpl

    @Before
    fun setUp() {
        spaceXApi = mockk()
        launchRepository = LaunchRepositoryImpl(spaceXApi)
        mockkObject(LaunchMapper)
    }

    @Test
    fun `getUpcomingLaunches should return success when API call succeeds`() =
        runTest {
            // Given
            val launchDtos =
                listOf(
                    createSampleLaunchDto("launch1", "Launch 1", upcoming = true),
                    createSampleLaunchDto("launch2", "Launch 2", upcoming = true),
                )
            val expectedLaunches =
                listOf(
                    createSampleLaunch("launch1", "Launch 1", upcoming = true),
                    createSampleLaunch("launch2", "Launch 2", upcoming = true),
                )

            coEvery { spaceXApi.getUpcomingLaunches() } returns launchDtos
            every { LaunchMapper.mapToDomain(launchDtos) } returns expectedLaunches

            // When
            val result = launchRepository.getUpcomingLaunches()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped launches", expectedLaunches, result.getOrNull())
            coVerify { spaceXApi.getUpcomingLaunches() }
        }

    @Test
    fun `getUpcomingLaunches should return failure when API call fails`() =
        runTest {
            // Given
            val exception = IOException("Network error")
            coEvery { spaceXApi.getUpcomingLaunches() } throws exception

            // When
            val result = launchRepository.getUpcomingLaunches()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getUpcomingLaunches() }
        }

    @Test
    fun `getUpcomingLaunches should return failure when mapper fails`() =
        runTest {
            // Given
            val launchDtos = listOf(createSampleLaunchDto("launch1", "Launch 1", upcoming = true))
            val exception = RuntimeException("Mapping error")

            coEvery { spaceXApi.getUpcomingLaunches() } returns launchDtos
            every { LaunchMapper.mapToDomain(launchDtos) } throws exception

            // When
            val result = launchRepository.getUpcomingLaunches()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getUpcomingLaunches() }
        }

    @Test
    fun `getPastLaunches should return success when API call succeeds`() =
        runTest {
            // Given
            val launchDtos =
                listOf(
                    createSampleLaunchDto("launch1", "Launch 1", upcoming = false),
                    createSampleLaunchDto("launch2", "Launch 2", upcoming = false),
                )
            val expectedLaunches =
                listOf(
                    createSampleLaunch("launch1", "Launch 1", upcoming = false),
                    createSampleLaunch("launch2", "Launch 2", upcoming = false),
                )

            coEvery { spaceXApi.getPastLaunches() } returns launchDtos
            every { LaunchMapper.mapToDomain(launchDtos) } returns expectedLaunches

            // When
            val result = launchRepository.getPastLaunches()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped launches", expectedLaunches, result.getOrNull())
            coVerify { spaceXApi.getPastLaunches() }
        }

    @Test
    fun `getPastLaunches should return failure when API call fails`() =
        runTest {
            // Given
            val exception = IOException("Network error")
            coEvery { spaceXApi.getPastLaunches() } throws exception

            // When
            val result = launchRepository.getPastLaunches()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getPastLaunches() }
        }

    @Test
    fun `getPastLaunches should return failure when mapper fails`() =
        runTest {
            // Given
            val launchDtos = listOf(createSampleLaunchDto("launch1", "Launch 1", upcoming = false))
            val exception = RuntimeException("Mapping error")

            coEvery { spaceXApi.getPastLaunches() } returns launchDtos
            every { LaunchMapper.mapToDomain(launchDtos) } throws exception

            // When
            val result = launchRepository.getPastLaunches()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getPastLaunches() }
        }

    @Test
    fun `getUpcomingLaunches should handle empty list from API`() =
        runTest {
            // Given
            val emptyLaunchDtos = emptyList<LaunchDto>()
            val emptyLaunches = emptyList<Launch>()

            coEvery { spaceXApi.getUpcomingLaunches() } returns emptyLaunchDtos
            every { LaunchMapper.mapToDomain(emptyLaunchDtos) } returns emptyLaunches

            // When
            val result = launchRepository.getUpcomingLaunches()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { spaceXApi.getUpcomingLaunches() }
        }

    @Test
    fun `getPastLaunches should handle empty list from API`() =
        runTest {
            // Given
            val emptyLaunchDtos = emptyList<LaunchDto>()
            val emptyLaunches = emptyList<Launch>()

            coEvery { spaceXApi.getPastLaunches() } returns emptyLaunchDtos
            every { LaunchMapper.mapToDomain(emptyLaunchDtos) } returns emptyLaunches

            // When
            val result = launchRepository.getPastLaunches()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { spaceXApi.getPastLaunches() }
        }

    private fun createSampleLaunchDto(
        id: String,
        name: String,
        upcoming: Boolean,
    ): LaunchDto =
        LaunchDto(
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
