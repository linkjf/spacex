package com.linkjf.spacex.launch.home.domain.usecase

import com.linkjf.spacex.launch.home.domain.model.Rocket
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
import java.io.IOException

class GetRocketsUseCaseImplTest {
    private lateinit var rocketRepository: RocketRepository
    private lateinit var getRocketsUseCase: GetRocketsUseCaseImpl

    @Before
    fun setUp() {
        rocketRepository = mockk()
        getRocketsUseCase = GetRocketsUseCaseImpl(rocketRepository)
    }

    @Test
    fun `invoke should return success when repository returns success`() =
        runTest {
            // Given
            val expectedRockets =
                listOf(
                    createSampleRocket("falcon9", "Falcon 9"),
                    createSampleRocket("falcon-heavy", "Falcon Heavy"),
                )
            coEvery { rocketRepository.getRockets() } returns Result.success(expectedRockets)

            // When
            val result = getRocketsUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected rockets", expectedRockets, result.getOrNull())
            coVerify { rocketRepository.getRockets() }
        }

    @Test
    fun `invoke should return failure when repository returns failure`() =
        runTest {
            // Given
            val exception = IOException("Network error")
            coEvery { rocketRepository.getRockets() } returns Result.failure(exception)

            // When
            val result = getRocketsUseCase()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { rocketRepository.getRockets() }
        }

    @Test
    fun `invoke should handle empty list from repository`() =
        runTest {
            // Given
            val emptyRockets = emptyList<Rocket>()
            coEvery { rocketRepository.getRockets() } returns Result.success(emptyRockets)

            // When
            val result = getRocketsUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { rocketRepository.getRockets() }
        }

    @Test
    fun `invoke should handle repository exception`() =
        runTest {
            // Given
            val exception = RuntimeException("Repository error")
            coEvery { rocketRepository.getRockets() } throws exception

            // When & Then - Exception should be thrown since use case doesn't catch it
            try {
                getRocketsUseCase()
                assertTrue("Should have thrown exception", false)
            } catch (e: RuntimeException) {
                assertEquals("Should return the exception", exception, e)
            }
            coVerify { rocketRepository.getRockets() }
        }

    @Test
    fun `invoke should be callable as function`() =
        runTest {
            // Given
            val expectedRockets = listOf(createSampleRocket("falcon9", "Falcon 9"))
            coEvery { rocketRepository.getRockets() } returns Result.success(expectedRockets)

            // When - Test that the use case can be called as a function
            val result = getRocketsUseCase()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return expected rockets", expectedRockets, result.getOrNull())
            coVerify { rocketRepository.getRockets() }
        }

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
}
