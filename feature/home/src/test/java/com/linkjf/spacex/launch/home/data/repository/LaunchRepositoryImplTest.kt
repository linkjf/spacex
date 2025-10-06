package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.LaunchLibraryLaunchMapper
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.home.data.remote.dto.AgencyTypeDto
import com.linkjf.spacex.launch.home.data.remote.dto.CelestialBodyDto
import com.linkjf.spacex.launch.home.data.remote.dto.CountryDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLibraryLaunchDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLibraryResponseDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLocationDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchPadDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchRocketDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchServiceProviderDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchStatusDto
import com.linkjf.spacex.launch.home.data.remote.dto.NetPrecisionDto
import com.linkjf.spacex.launch.home.data.remote.dto.RocketConfigurationDto
import com.linkjf.spacex.launch.home.data.remote.dto.RocketFamilyDto
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
            val launchDtos =
                listOf(
                    createSampleLaunchLibraryDto("launch1", "Launch 1"),
                    createSampleLaunchLibraryDto("launch2", "Launch 2"),
                )
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

            coEvery { launchLibraryApi.getUpcomingLaunches() } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(launchDtos, true) } returns expectedLaunches

            val result = launchRepository.getUpcomingLaunches()

            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped launches", expectedLaunches, result.getOrNull())
            coVerify { launchLibraryApi.getUpcomingLaunches() }
        }

    @Test
    fun `getUpcomingLaunches should return failure when API call fails`() =
        runTest {
            val exception = IOException("Network error")
            coEvery { launchLibraryApi.getUpcomingLaunches() } throws exception

            val result = launchRepository.getUpcomingLaunches()

            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchLibraryApi.getUpcomingLaunches() }
        }

    @Test
    fun `getUpcomingLaunches should return failure when mapper fails`() =
        runTest {
            val launchDtos = listOf(createSampleLaunchLibraryDto("launch1", "Launch 1"))
            val apiResponse =
                LaunchLibraryResponseDto(
                    count = 1,
                    next = null,
                    previous = null,
                    results = launchDtos,
                )
            val exception = RuntimeException("Mapping error")

            coEvery { launchLibraryApi.getUpcomingLaunches() } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(launchDtos) } throws exception

            val result = launchRepository.getUpcomingLaunches()

            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchLibraryApi.getUpcomingLaunches() }
        }

    @Test
    fun `getPastLaunches should return success when API call succeeds`() =
        runTest {
            val launchDtos =
                listOf(
                    createSampleLaunchLibraryDto("launch1", "Launch 1"),
                    createSampleLaunchLibraryDto("launch2", "Launch 2"),
                )
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

            coEvery { launchLibraryApi.getPastLaunches() } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(launchDtos, false) } returns expectedLaunches

            val result = launchRepository.getPastLaunches()

            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped launches", expectedLaunches, result.getOrNull())
            coVerify { launchLibraryApi.getPastLaunches() }
        }

    @Test
    fun `getPastLaunches should return failure when API call fails`() =
        runTest {
            val exception = IOException("Network error")
            coEvery { launchLibraryApi.getPastLaunches() } throws exception

            val result = launchRepository.getPastLaunches()

            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchLibraryApi.getPastLaunches() }
        }

    @Test
    fun `getPastLaunches should return failure when mapper fails`() =
        runTest {
            val launchDtos = listOf(createSampleLaunchLibraryDto("launch1", "Launch 1"))
            val apiResponse =
                LaunchLibraryResponseDto(
                    count = 1,
                    next = null,
                    previous = null,
                    results = launchDtos,
                )
            val exception = RuntimeException("Mapping error")

            coEvery { launchLibraryApi.getPastLaunches() } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(launchDtos, false) } throws exception

            val result = launchRepository.getPastLaunches()

            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { launchLibraryApi.getPastLaunches() }
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

            coEvery { launchLibraryApi.getUpcomingLaunches() } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(emptyLaunchDtos, true) } returns emptyLaunches

            val result = launchRepository.getUpcomingLaunches()

            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { launchLibraryApi.getUpcomingLaunches() }
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

            coEvery { launchLibraryApi.getPastLaunches() } returns apiResponse
            every { LaunchLibraryLaunchMapper.mapToDomain(emptyLaunchDtos, false) } returns emptyLaunches

            val result = launchRepository.getPastLaunches()

            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { launchLibraryApi.getPastLaunches() }
        }

    private fun createSampleLaunchLibraryDto(
        id: String,
        name: String,
    ): LaunchLibraryLaunchDto =
        LaunchLibraryLaunchDto(
            id = id,
            url = "https://example.com/launch/$id",
            name = name,
            responseMode = "normal",
            slug = name.lowercase().replace(" ", "-"),
            launchDesignator = null,
            status =
                LaunchStatusDto(
                    id = 1,
                    name = "Go for Launch",
                    abbrev = "Go",
                    description = "Current T-0 confirmed by official or reliable sources.",
                ),
            lastUpdated = "2023-01-01T00:00:00Z",
            net = "2023-01-01T00:00:00Z",
            netPrecision =
                NetPrecisionDto(
                    id = 1,
                    name = "Minute",
                    abbrev = "MIN",
                    description = "The T-0 is accurate to the minute.",
                ),
            windowEnd = null,
            windowStart = null,
            image = null,
            infographic = null,
            probability = null,
            weatherConcerns = null,
            failReason = "",
            hashtag = null,
            launchServiceProvider =
                LaunchServiceProviderDto(
                    responseMode = "list",
                    id = 121,
                    url = "https://example.com/launch/$id",
                    name = "SpaceX",
                    abbrev = "SpX",
                    type =
                        AgencyTypeDto(
                            id = 3,
                            name = "Commercial",
                        ),
                ),
            rocket =
                LaunchRocketDto(
                    id = 8724,
                    configuration =
                        RocketConfigurationDto(
                            responseMode = "list",
                            id = 164,
                            url = "https://example.com/launch/$id",
                            name = "Falcon 9",
                            families =
                                listOf(
                                    RocketFamilyDto(
                                        responseMode = "list",
                                        id = 1,
                                        name = "Falcon",
                                    ),
                                ),
                            fullName = "Falcon 9 Block 5",
                            variant = "Block 5",
                        ),
                ),
            mission = null,
            pad =
                LaunchPadDto(
                    id = 80,
                    url = "https://example.com/launch/$id",
                    active = true,
                    agencies = null,
                    name = "Space Launch Complex 40",
                    image = null,
                    description = "",
                    infoUrl = null,
                    wikiUrl = "https://example.com/wiki",
                    mapUrl = "https://example.com/map",
                    latitude = 28.56194122,
                    longitude = -80.57735736,
                    country =
                        CountryDto(
                            id = 2,
                            name = "United States of America",
                            alpha2Code = "US",
                            alpha3Code = "USA",
                            nationalityName = "American",
                            nationalityNameComposed = "Americano",
                        ),
                    mapImage = null,
                    totalLaunchCount = 337,
                    orbitalLaunchAttemptCount = 337,
                    fastestTurnaround = null,
                    location =
                        LaunchLocationDto(
                            responseMode = "normal",
                            id = 12,
                            url = "https://example.com/launch/$id",
                            name = "Cape Canaveral SFS, FL, USA",
                            celestialBody =
                                CelestialBodyDto(
                                    responseMode = "normal",
                                    id = 1,
                                    name = "Earth",
                                ),
                            active = true,
                            country =
                                CountryDto(
                                    id = 2,
                                    name = "United States of America",
                                    alpha2Code = "US",
                                    alpha3Code = "USA",
                                    nationalityName = "American",
                                    nationalityNameComposed = "Americano",
                                ),
                            description = null,
                            image = null,
                            mapImage = null,
                            longitude = -80.577778,
                            latitude = 28.488889,
                            timezoneName = "America/New_York",
                            totalLaunchCount = 1056,
                            totalLandingCount = null,
                        ),
                ),
            webcastLive = false,
            program = null,
            orbitalLaunchAttemptCount = null,
            locationLaunchAttemptCount = null,
            padLaunchAttemptCount = null,
            agencyLaunchAttemptCount = null,
            orbitalLaunchAttemptCountYear = null,
            locationLaunchAttemptCountYear = null,
            padLaunchAttemptCountYear = null,
            agencyLaunchAttemptCountYear = null,
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
