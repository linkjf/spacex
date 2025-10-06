package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.model.Rocket
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import com.linkjf.spacex.launch.home.domain.repository.LaunchpadRepository
import com.linkjf.spacex.launch.home.domain.repository.RocketRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UseCaseIntegrationTest {
    private lateinit var launchRepository: LaunchRepository
    private lateinit var rocketRepository: RocketRepository
    private lateinit var launchpadRepository: LaunchpadRepository

    private lateinit var getUpcomingLaunchesUseCase: GetUpcomingLaunchesUseCaseImpl
    private lateinit var getPastLaunchesUseCase: GetPastLaunchesUseCaseImpl
    private lateinit var getRocketsUseCase: GetRocketsUseCaseImpl
    private lateinit var getRocketUseCase: GetRocketUseCaseImpl
    private lateinit var getLaunchpadsUseCase: GetLaunchpadsUseCaseImpl
    private lateinit var getLaunchpadUseCase: GetLaunchpadUseCaseImpl

    @Before
    fun setUp() {
        launchRepository = mockk()
        rocketRepository = mockk()
        launchpadRepository = mockk()

        getUpcomingLaunchesUseCase = GetUpcomingLaunchesUseCaseImpl(launchRepository)
        getPastLaunchesUseCase = GetPastLaunchesUseCaseImpl(launchRepository)
        getRocketsUseCase = GetRocketsUseCaseImpl(rocketRepository)
        getRocketUseCase = GetRocketUseCaseImpl(rocketRepository)
        getLaunchpadsUseCase = GetLaunchpadsUseCaseImpl(launchpadRepository)
        getLaunchpadUseCase = GetLaunchpadUseCaseImpl(launchpadRepository)
    }

    @Test
    fun `all use cases should work together successfully`() =
        runTest {
            // Given - Mock repository responses
            val upcomingLaunches = listOf(createSampleLaunch("launch1", "Upcoming Launch", upcoming = true))
            val pastLaunches = listOf(createSampleLaunch("launch2", "Past Launch", upcoming = false))
            val rockets = listOf(createSampleRocket("falcon9", "Falcon 9"))
            val rocket = createSampleRocket("falcon9", "Falcon 9")
            val launchpads = listOf(createSampleLaunchpad("ksc_lc_39a", "KSC LC 39A"))
            val launchpad = createSampleLaunchpad("ksc_lc_39a", "KSC LC 39A")

            // Mock repository calls
            coEvery { launchRepository.getUpcomingLaunches() } returns Result.success(upcomingLaunches)
            coEvery { launchRepository.getPastLaunches() } returns Result.success(pastLaunches)
            coEvery { rocketRepository.getRockets() } returns Result.success(rockets)
            coEvery { rocketRepository.getRocket("falcon9") } returns Result.success(rocket)
            coEvery { launchpadRepository.getLaunchpads() } returns Result.success(launchpads)
            coEvery { launchpadRepository.getLaunchpad("ksc_lc_39a") } returns Result.success(launchpad)

            // When - Test all use cases
            val upcomingResult = getUpcomingLaunchesUseCase()
            val pastResult = getPastLaunchesUseCase()
            val rocketsResult = getRocketsUseCase()
            val rocketResult = getRocketUseCase("falcon9")
            val launchpadsResult = getLaunchpadsUseCase()
            val launchpadResult = getLaunchpadUseCase("ksc_lc_39a")

            // Then - Verify all results are successful
            assertTrue("Upcoming launches should be successful", upcomingResult.isSuccess)
            assertTrue("Past launches should be successful", pastResult.isSuccess)
            assertTrue("Rockets should be successful", rocketsResult.isSuccess)
            assertTrue("Single rocket should be successful", rocketResult.isSuccess)
            assertTrue("Launchpads should be successful", launchpadsResult.isSuccess)
            assertTrue("Single launchpad should be successful", launchpadResult.isSuccess)

            // Verify returned data
            assertEquals("Should return expected upcoming launches", upcomingLaunches, upcomingResult.getOrNull())
            assertEquals("Should return expected past launches", pastLaunches, pastResult.getOrNull())
            assertEquals("Should return expected rockets", rockets, rocketsResult.getOrNull())
            assertEquals("Should return expected rocket", rocket, rocketResult.getOrNull())
            assertEquals("Should return expected launchpads", launchpads, launchpadsResult.getOrNull())
            assertEquals("Should return expected launchpad", launchpad, launchpadResult.getOrNull())

            // Verify all repository calls were made
            coVerify { launchRepository.getUpcomingLaunches() }
            coVerify { launchRepository.getPastLaunches() }
            coVerify { rocketRepository.getRockets() }
            coVerify { rocketRepository.getRocket("falcon9") }
            coVerify { launchpadRepository.getLaunchpads() }
            coVerify { launchpadRepository.getLaunchpad("ksc_lc_39a") }
        }

    @Test
    fun `use cases should handle repository failures gracefully`() =
        runTest {
            // Given - Mock repositories to return failures
            coEvery { launchRepository.getUpcomingLaunches() } returns Result.failure(Exception("API Error"))
            coEvery { launchRepository.getPastLaunches() } returns Result.failure(Exception("API Error"))
            coEvery { rocketRepository.getRockets() } returns Result.failure(Exception("API Error"))
            coEvery { rocketRepository.getRocket("falcon9") } returns Result.failure(Exception("API Error"))
            coEvery { launchpadRepository.getLaunchpads() } returns Result.failure(Exception("API Error"))
            coEvery { launchpadRepository.getLaunchpad("ksc_lc_39a") } returns Result.failure(Exception("API Error"))

            // When - Test all use cases
            val upcomingResult = getUpcomingLaunchesUseCase()
            val pastResult = getPastLaunchesUseCase()
            val rocketsResult = getRocketsUseCase()
            val rocketResult = getRocketUseCase("falcon9")
            val launchpadsResult = getLaunchpadsUseCase()
            val launchpadResult = getLaunchpadUseCase("ksc_lc_39a")

            // Then - Verify all results are failures
            assertTrue("Upcoming launches should be failure", upcomingResult.isFailure)
            assertTrue("Past launches should be failure", pastResult.isFailure)
            assertTrue("Rockets should be failure", rocketsResult.isFailure)
            assertTrue("Single rocket should be failure", rocketResult.isFailure)
            assertTrue("Launchpads should be failure", launchpadsResult.isFailure)
            assertTrue("Single launchpad should be failure", launchpadResult.isFailure)

            // Verify all repository calls were made
            coVerify { launchRepository.getUpcomingLaunches() }
            coVerify { launchRepository.getPastLaunches() }
            coVerify { rocketRepository.getRockets() }
            coVerify { rocketRepository.getRocket("falcon9") }
            coVerify { launchpadRepository.getLaunchpads() }
            coVerify { launchpadRepository.getLaunchpad("ksc_lc_39a") }
        }

    @Test
    fun `use cases should handle empty responses correctly`() =
        runTest {
            // Given - Mock repositories to return empty lists
            val emptyLaunches = emptyList<Launch>()
            val emptyRockets = emptyList<Rocket>()
            val emptyLaunchpads = emptyList<Launchpad>()

            coEvery { launchRepository.getUpcomingLaunches() } returns Result.success(emptyLaunches)
            coEvery { launchRepository.getPastLaunches() } returns Result.success(emptyLaunches)
            coEvery { rocketRepository.getRockets() } returns Result.success(emptyRockets)
            coEvery { launchpadRepository.getLaunchpads() } returns Result.success(emptyLaunchpads)

            // When - Test all use cases
            val upcomingResult = getUpcomingLaunchesUseCase()
            val pastResult = getPastLaunchesUseCase()
            val rocketsResult = getRocketsUseCase()
            val launchpadsResult = getLaunchpadsUseCase()

            // Then - Verify all results are successful with empty data
            assertTrue("Upcoming launches should be successful", upcomingResult.isSuccess)
            assertTrue("Past launches should be successful", pastResult.isSuccess)
            assertTrue("Rockets should be successful", rocketsResult.isSuccess)
            assertTrue("Launchpads should be successful", launchpadsResult.isSuccess)

            assertNotNull("Upcoming launches result should not be null", upcomingResult.getOrNull())
            assertNotNull("Past launches result should not be null", pastResult.getOrNull())
            assertNotNull("Rockets result should not be null", rocketsResult.getOrNull())
            assertNotNull("Launchpads result should not be null", launchpadsResult.getOrNull())

            assertTrue("Upcoming launches should be empty", upcomingResult.getOrNull()?.isEmpty() == true)
            assertTrue("Past launches should be empty", pastResult.getOrNull()?.isEmpty() == true)
            assertTrue("Rockets should be empty", rocketsResult.getOrNull()?.isEmpty() == true)
            assertTrue("Launchpads should be empty", launchpadsResult.getOrNull()?.isEmpty() == true)
        }

    @Test
    fun `use cases should be callable as functions`() =
        runTest {
            // Given
            val upcomingLaunches = listOf(createSampleLaunch("launch1", "Upcoming Launch", upcoming = true))
            val rockets = listOf(createSampleRocket("falcon9", "Falcon 9"))
            val launchpads = listOf(createSampleLaunchpad("ksc_lc_39a", "KSC LC 39A"))

            coEvery { launchRepository.getUpcomingLaunches() } returns Result.success(upcomingLaunches)
            coEvery { rocketRepository.getRockets() } returns Result.success(rockets)
            coEvery { launchpadRepository.getLaunchpads() } returns Result.success(launchpads)

            // When - Test function call syntax
            val upcomingResult = getUpcomingLaunchesUseCase()
            val rocketsResult = getRocketsUseCase()
            val launchpadsResult = getLaunchpadsUseCase()

            // Then - Verify function call syntax works
            assertTrue("Upcoming launches should be successful", upcomingResult.isSuccess)
            assertTrue("Rockets should be successful", rocketsResult.isSuccess)
            assertTrue("Launchpads should be successful", launchpadsResult.isSuccess)

            assertEquals("Should return expected upcoming launches", upcomingLaunches, upcomingResult.getOrNull())
            assertEquals("Should return expected rockets", rockets, rocketsResult.getOrNull())
            assertEquals("Should return expected launchpads", launchpads, launchpadsResult.getOrNull())
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

    private fun createSampleRocket(
        id: String,
        name: String,
    ): Rocket =
        Rocket(
            id = id,
            name = name,
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
