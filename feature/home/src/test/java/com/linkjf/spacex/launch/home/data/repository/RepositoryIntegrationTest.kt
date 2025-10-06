package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.LaunchMapper
import com.linkjf.spacex.launch.home.data.mapper.LaunchpadMapper
import com.linkjf.spacex.launch.home.data.mapper.RocketMapper
import com.linkjf.spacex.launch.home.data.remote.SpaceXApi
import com.linkjf.spacex.launch.home.data.remote.dto.*
import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.model.Rocket
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RepositoryIntegrationTest {
    private lateinit var spaceXApi: SpaceXApi
    private lateinit var launchRepository: LaunchRepositoryImpl
    private lateinit var rocketRepository: RocketRepositoryImpl
    private lateinit var launchpadRepository: LaunchpadRepositoryImpl

    @Before
    fun setUp() {
        spaceXApi = mockk()
        launchRepository = LaunchRepositoryImpl(spaceXApi)
        rocketRepository = RocketRepositoryImpl(spaceXApi)
        launchpadRepository = LaunchpadRepositoryImpl(spaceXApi)

        mockkObject(LaunchMapper)
        mockkObject(RocketMapper)
        mockkObject(LaunchpadMapper)
    }

    @Test
    fun `all repositories should work together successfully`() =
        runTest {
            // Given - Mock API responses
            val launchDtos = listOf(createSampleLaunchDto())
            val rocketDtos = listOf(createSampleRocketDto())
            val launchpadDtos = listOf(createSampleLaunchpadDto())

            val expectedLaunches = listOf(createSampleLaunch())
            val expectedRockets = listOf(createSampleRocket())
            val expectedLaunchpads = listOf(createSampleLaunchpad())

            // Mock API calls
            coEvery { spaceXApi.getUpcomingLaunches() } returns launchDtos
            coEvery { spaceXApi.getPastLaunches() } returns launchDtos
            coEvery { spaceXApi.getRockets() } returns rocketDtos
            coEvery { spaceXApi.getRocket("falcon9") } returns rocketDtos[0]
            coEvery { spaceXApi.getLaunchpads() } returns launchpadDtos
            coEvery { spaceXApi.getLaunchpad("ksc_lc_39a") } returns launchpadDtos[0]

            // Mock mappers
            every { LaunchMapper.mapToDomain(launchDtos) } returns expectedLaunches
            every { RocketMapper.mapToDomain(rocketDtos) } returns expectedRockets
            every { RocketMapper.mapToDomain(rocketDtos[0]) } returns expectedRockets[0]
            every { LaunchpadMapper.mapToDomain(launchpadDtos) } returns expectedLaunchpads
            every { LaunchpadMapper.mapToDomain(launchpadDtos[0]) } returns expectedLaunchpads[0]

            // When - Test all repository methods
            val upcomingLaunchesResult = launchRepository.getUpcomingLaunches()
            val pastLaunchesResult = launchRepository.getPastLaunches()
            val rocketsResult = rocketRepository.getRockets()
            val rocketResult = rocketRepository.getRocket("falcon9")
            val launchpadsResult = launchpadRepository.getLaunchpads()
            val launchpadResult = launchpadRepository.getLaunchpad("ksc_lc_39a")

            // Then - Verify all results are successful
            assertTrue("Upcoming launches should be successful", upcomingLaunchesResult.isSuccess)
            assertTrue("Past launches should be successful", pastLaunchesResult.isSuccess)
            assertTrue("Rockets should be successful", rocketsResult.isSuccess)
            assertTrue("Single rocket should be successful", rocketResult.isSuccess)
            assertTrue("Launchpads should be successful", launchpadsResult.isSuccess)
            assertTrue("Single launchpad should be successful", launchpadResult.isSuccess)

            // Verify returned data
            assertEquals("Should return expected launches", expectedLaunches, upcomingLaunchesResult.getOrNull())
            assertEquals("Should return expected launches", expectedLaunches, pastLaunchesResult.getOrNull())
            assertEquals("Should return expected rockets", expectedRockets, rocketsResult.getOrNull())
            assertEquals("Should return expected rocket", expectedRockets[0], rocketResult.getOrNull())
            assertEquals("Should return expected launchpads", expectedLaunchpads, launchpadsResult.getOrNull())
            assertEquals("Should return expected launchpad", expectedLaunchpads[0], launchpadResult.getOrNull())

            // Verify all API calls were made
            coVerify { spaceXApi.getUpcomingLaunches() }
            coVerify { spaceXApi.getPastLaunches() }
            coVerify { spaceXApi.getRockets() }
            coVerify { spaceXApi.getRocket("falcon9") }
            coVerify { spaceXApi.getLaunchpads() }
            coVerify { spaceXApi.getLaunchpad("ksc_lc_39a") }
        }

    @Test
    fun `repositories should handle API failures gracefully`() =
        runTest {
            // Given - Mock API to throw exceptions
            coEvery { spaceXApi.getUpcomingLaunches() } throws Exception("API Error")
            coEvery { spaceXApi.getPastLaunches() } throws Exception("API Error")
            coEvery { spaceXApi.getRockets() } throws Exception("API Error")
            coEvery { spaceXApi.getRocket("falcon9") } throws Exception("API Error")
            coEvery { spaceXApi.getLaunchpads() } throws Exception("API Error")
            coEvery { spaceXApi.getLaunchpad("ksc_lc_39a") } throws Exception("API Error")

            // When - Test all repository methods
            val upcomingLaunchesResult = launchRepository.getUpcomingLaunches()
            val pastLaunchesResult = launchRepository.getPastLaunches()
            val rocketsResult = rocketRepository.getRockets()
            val rocketResult = rocketRepository.getRocket("falcon9")
            val launchpadsResult = launchpadRepository.getLaunchpads()
            val launchpadResult = launchpadRepository.getLaunchpad("ksc_lc_39a")

            // Then - Verify all results are failures
            assertTrue("Upcoming launches should be failure", upcomingLaunchesResult.isFailure)
            assertTrue("Past launches should be failure", pastLaunchesResult.isFailure)
            assertTrue("Rockets should be failure", rocketsResult.isFailure)
            assertTrue("Single rocket should be failure", rocketResult.isFailure)
            assertTrue("Launchpads should be failure", launchpadsResult.isFailure)
            assertTrue("Single launchpad should be failure", launchpadResult.isFailure)

            // Verify all API calls were made
            coVerify { spaceXApi.getUpcomingLaunches() }
            coVerify { spaceXApi.getPastLaunches() }
            coVerify { spaceXApi.getRockets() }
            coVerify { spaceXApi.getRocket("falcon9") }
            coVerify { spaceXApi.getLaunchpads() }
            coVerify { spaceXApi.getLaunchpad("ksc_lc_39a") }
        }

    @Test
    fun `repositories should handle empty responses correctly`() =
        runTest {
            // Given - Mock API to return empty lists
            val emptyLaunchDtos = emptyList<LaunchDto>()
            val emptyRocketDtos = emptyList<RocketDto>()
            val emptyLaunchpadDtos = emptyList<LaunchpadDto>()

            val emptyLaunches = emptyList<Launch>()
            val emptyRockets = emptyList<Rocket>()
            val emptyLaunchpads = emptyList<Launchpad>()

            coEvery { spaceXApi.getUpcomingLaunches() } returns emptyLaunchDtos
            coEvery { spaceXApi.getPastLaunches() } returns emptyLaunchDtos
            coEvery { spaceXApi.getRockets() } returns emptyRocketDtos
            coEvery { spaceXApi.getLaunchpads() } returns emptyLaunchpadDtos

            every { LaunchMapper.mapToDomain(emptyLaunchDtos) } returns emptyLaunches
            every { RocketMapper.mapToDomain(emptyRocketDtos) } returns emptyRockets
            every { LaunchpadMapper.mapToDomain(emptyLaunchpadDtos) } returns emptyLaunchpads

            // When - Test all repository methods
            val upcomingLaunchesResult = launchRepository.getUpcomingLaunches()
            val pastLaunchesResult = launchRepository.getPastLaunches()
            val rocketsResult = rocketRepository.getRockets()
            val launchpadsResult = launchpadRepository.getLaunchpads()

            // Then - Verify all results are successful with empty data
            assertTrue("Upcoming launches should be successful", upcomingLaunchesResult.isSuccess)
            assertTrue("Past launches should be successful", pastLaunchesResult.isSuccess)
            assertTrue("Rockets should be successful", rocketsResult.isSuccess)
            assertTrue("Launchpads should be successful", launchpadsResult.isSuccess)

            assertNotNull("Upcoming launches result should not be null", upcomingLaunchesResult.getOrNull())
            assertNotNull("Past launches result should not be null", pastLaunchesResult.getOrNull())
            assertNotNull("Rockets result should not be null", rocketsResult.getOrNull())
            assertNotNull("Launchpads result should not be null", launchpadsResult.getOrNull())

            assertTrue("Upcoming launches should be empty", upcomingLaunchesResult.getOrNull()?.isEmpty() == true)
            assertTrue("Past launches should be empty", pastLaunchesResult.getOrNull()?.isEmpty() == true)
            assertTrue("Rockets should be empty", rocketsResult.getOrNull()?.isEmpty() == true)
            assertTrue("Launchpads should be empty", launchpadsResult.getOrNull()?.isEmpty() == true)
        }

    private fun createSampleLaunchDto(): LaunchDto =
        LaunchDto(
            id = "test-launch",
            name = "Test Launch",
            dateUtc = "2023-01-01T00:00:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links = null,
            details = null,
            success = null,
            upcoming = true,
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

    private fun createSampleLaunch(): Launch =
        Launch(
            id = "test-launch",
            name = "Test Launch",
            dateUtc = "2023-01-01T00:00:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links = null,
            details = null,
            success = null,
            upcoming = true,
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

    private fun createSampleRocketDto(): RocketDto =
        RocketDto(
            id = "falcon9",
            name = "Falcon 9",
            type = "rocket",
            active = true,
            stages = 2,
            boosters = 0,
            costPerLaunch = 6700000,
            successRatePct = 98.0,
            firstFlight = "2010-06-04",
            country = "Republic of the Marshall Islands",
            company = "SpaceX",
            height = DimensionDto(meters = 70.0, feet = 229.6),
            diameter = DimensionDto(meters = 3.7, feet = 12.0),
            mass = MassDto(kg = 549054, lb = 1207920),
            payloadWeights = emptyList(),
            firstStage =
                StageDto(
                    reusable = true,
                    engines = 9,
                    fuelAmountTons = 385.0,
                    burnTimeSec = 162,
                    thrustSeaLevel = ThrustDto(kN = 7607, lbf = 1710000),
                    thrustVacuum = ThrustDto(kN = 8227, lbf = 1849500),
                ),
            secondStage =
                StageDto(
                    reusable = false,
                    engines = 1,
                    fuelAmountTons = 90.0,
                    burnTimeSec = 397,
                    thrustSeaLevel = ThrustDto(kN = 934, lbf = 210000),
                    thrustVacuum = ThrustDto(kN = 934, lbf = 210000),
                ),
            engines =
                EngineDto(
                    isp = IspDto(seaLevel = 282, vacuum = 311),
                    thrustSeaLevel = ThrustDto(kN = 845, lbf = 190000),
                    thrustVacuum = ThrustDto(kN = 914, lbf = 205500),
                    number = 9,
                    type = "merlin",
                    version = "1D+",
                    layout = "octaweb",
                    engineLossMax = 2,
                    propellant1 = "liquid oxygen",
                    propellant2 = "RP-1 kerosene",
                    thrustToWeight = 180.1,
                ),
            landingLegs = LandingLegsDto(number = 4, material = "carbon fiber"),
            flickrImages = emptyList(),
            wikipedia = "https://en.wikipedia.org/wiki/Falcon_9",
            description = "Test rocket description",
        )

    private fun createSampleRocket(): Rocket =
        Rocket(
            id = "falcon9",
            name = "Falcon 9",
            type = "rocket",
            active = true,
            stages = 2,
            boosters = 0,
            costPerLaunch = 6700000,
            successRatePct = 98.0,
            firstFlight = "2010-06-04",
            country = "Republic of the Marshall Islands",
            company = "SpaceX",
            height =
                com.linkjf.spacex.launch.home.domain.model
                    .Dimension(meters = 70.0, feet = 229.6),
            diameter =
                com.linkjf.spacex.launch.home.domain.model
                    .Dimension(meters = 3.7, feet = 12.0),
            mass =
                com.linkjf.spacex.launch.home.domain.model
                    .Mass(kg = 549054, lb = 1207920),
            payloadWeights = emptyList(),
            firstStage =
                com.linkjf.spacex.launch.home.domain.model.Stage(
                    reusable = true,
                    engines = 9,
                    fuelAmountTons = 385.0,
                    burnTimeSec = 162,
                    thrustSeaLevel =
                        com.linkjf.spacex.launch.home.domain.model
                            .Thrust(kN = 7607, lbf = 1710000),
                    thrustVacuum =
                        com.linkjf.spacex.launch.home.domain.model
                            .Thrust(kN = 8227, lbf = 1849500),
                ),
            secondStage =
                com.linkjf.spacex.launch.home.domain.model.Stage(
                    reusable = false,
                    engines = 1,
                    fuelAmountTons = 90.0,
                    burnTimeSec = 397,
                    thrustSeaLevel =
                        com.linkjf.spacex.launch.home.domain.model
                            .Thrust(kN = 934, lbf = 210000),
                    thrustVacuum =
                        com.linkjf.spacex.launch.home.domain.model
                            .Thrust(kN = 934, lbf = 210000),
                ),
            engines =
                com.linkjf.spacex.launch.home.domain.model.Engine(
                    isp =
                        com.linkjf.spacex.launch.home.domain.model
                            .Isp(seaLevel = 282, vacuum = 311),
                    thrustSeaLevel =
                        com.linkjf.spacex.launch.home.domain.model
                            .Thrust(kN = 845, lbf = 190000),
                    thrustVacuum =
                        com.linkjf.spacex.launch.home.domain.model
                            .Thrust(kN = 914, lbf = 205500),
                    number = 9,
                    type = "merlin",
                    version = "1D+",
                    layout = "octaweb",
                    engineLossMax = 2,
                    propellant1 = "liquid oxygen",
                    propellant2 = "RP-1 kerosene",
                    thrustToWeight = 180.1,
                ),
            landingLegs =
                com.linkjf.spacex.launch.home.domain.model
                    .LandingLegs(number = 4, material = "carbon fiber"),
            flickrImages = emptyList(),
            wikipedia = "https://en.wikipedia.org/wiki/Falcon_9",
            description = "Test rocket description",
        )

    private fun createSampleLaunchpadDto(): LaunchpadDto =
        LaunchpadDto(
            id = "ksc_lc_39a",
            name = "KSC LC 39A",
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
            details = "SpaceX historic launch site",
        )

    private fun createSampleLaunchpad(): Launchpad =
        Launchpad(
            id = "ksc_lc_39a",
            name = "KSC LC 39A",
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
            details = "SpaceX historic launch site",
        )
}
