package com.linkjf.spacex.launch.home.data.repository

import com.linkjf.spacex.launch.home.data.mapper.RocketMapper
import com.linkjf.spacex.launch.home.data.remote.SpaceXApi
import com.linkjf.spacex.launch.home.data.remote.dto.*
import com.linkjf.spacex.launch.home.domain.model.Rocket
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

class RocketRepositoryImplTest {
    private lateinit var spaceXApi: SpaceXApi
    private lateinit var rocketRepository: RocketRepositoryImpl

    @Before
    fun setUp() {
        spaceXApi = mockk()
        rocketRepository = RocketRepositoryImpl(spaceXApi)
        mockkObject(RocketMapper)
    }

    @Test
    fun `getRockets should return success when API call succeeds`() =
        runTest {
            // Given
            val rocketDtos =
                listOf(
                    createSampleRocketDto("falcon9", "Falcon 9"),
                    createSampleRocketDto("falcon-heavy", "Falcon Heavy"),
                )
            val expectedRockets =
                listOf(
                    createSampleRocket("falcon9", "Falcon 9"),
                    createSampleRocket("falcon-heavy", "Falcon Heavy"),
                )

            coEvery { spaceXApi.getRockets() } returns rocketDtos
            every { RocketMapper.mapToDomain(rocketDtos) } returns expectedRockets

            // When
            val result = rocketRepository.getRockets()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped rockets", expectedRockets, result.getOrNull())
            coVerify { spaceXApi.getRockets() }
        }

    @Test
    fun `getRockets should return failure when API call fails`() =
        runTest {
            // Given
            val exception = IOException("Network error")
            coEvery { spaceXApi.getRockets() } throws exception

            // When
            val result = rocketRepository.getRockets()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getRockets() }
        }

    @Test
    fun `getRockets should return failure when mapper fails`() =
        runTest {
            // Given
            val rocketDtos = listOf(createSampleRocketDto("falcon9", "Falcon 9"))
            val exception = RuntimeException("Mapping error")

            coEvery { spaceXApi.getRockets() } returns rocketDtos
            every { RocketMapper.mapToDomain(rocketDtos) } throws exception

            // When
            val result = rocketRepository.getRockets()

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getRockets() }
        }

    @Test
    fun `getRocket should return success when API call succeeds`() =
        runTest {
            // Given
            val rocketId = "falcon9"
            val rocketDto = createSampleRocketDto(rocketId, "Falcon 9")
            val expectedRocket = createSampleRocket(rocketId, "Falcon 9")

            coEvery { spaceXApi.getRocket(rocketId) } returns rocketDto
            every { RocketMapper.mapToDomain(rocketDto) } returns expectedRocket

            // When
            val result = rocketRepository.getRocket(rocketId)

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertEquals("Should return mapped rocket", expectedRocket, result.getOrNull())
            coVerify { spaceXApi.getRocket(rocketId) }
        }

    @Test
    fun `getRocket should return failure when API call fails`() =
        runTest {
            // Given
            val rocketId = "falcon9"
            val exception = IOException("Network error")
            coEvery { spaceXApi.getRocket(rocketId) } throws exception

            // When
            val result = rocketRepository.getRocket(rocketId)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getRocket(rocketId) }
        }

    @Test
    fun `getRocket should return failure when mapper fails`() =
        runTest {
            // Given
            val rocketId = "falcon9"
            val rocketDto = createSampleRocketDto(rocketId, "Falcon 9")
            val exception = RuntimeException("Mapping error")

            coEvery { spaceXApi.getRocket(rocketId) } returns rocketDto
            every { RocketMapper.mapToDomain(rocketDto) } throws exception

            // When
            val result = rocketRepository.getRocket(rocketId)

            // Then
            assertTrue("Result should be failure", result.isFailure)
            assertEquals("Should return the exception", exception, result.exceptionOrNull())
            coVerify { spaceXApi.getRocket(rocketId) }
        }

    @Test
    fun `getRockets should handle empty list from API`() =
        runTest {
            // Given
            val emptyRocketDtos = emptyList<RocketDto>()
            val emptyRockets = emptyList<Rocket>()

            coEvery { spaceXApi.getRockets() } returns emptyRocketDtos
            every { RocketMapper.mapToDomain(emptyRocketDtos) } returns emptyRockets

            // When
            val result = rocketRepository.getRockets()

            // Then
            assertTrue("Result should be success", result.isSuccess)
            assertNotNull("Result should not be null", result.getOrNull())
            assertTrue("Should return empty list", result.getOrNull()?.isEmpty() == true)
            coVerify { spaceXApi.getRockets() }
        }

    @Test
    fun `getRocket should handle different rocket IDs`() =
        runTest {
            // Given
            val rocketIds = listOf("falcon9", "falcon-heavy", "starship")

            rocketIds.forEach { rocketId ->
                val rocketDto = createSampleRocketDto(rocketId, "Rocket $rocketId")
                val expectedRocket = createSampleRocket(rocketId, "Rocket $rocketId")

                coEvery { spaceXApi.getRocket(rocketId) } returns rocketDto
                every { RocketMapper.mapToDomain(rocketDto) } returns expectedRocket

                // When
                val result = rocketRepository.getRocket(rocketId)

                // Then
                assertTrue("Result should be success for $rocketId", result.isSuccess)
                assertEquals("Should return correct rocket for $rocketId", expectedRocket, result.getOrNull())
                coVerify { spaceXApi.getRocket(rocketId) }
            }
        }

    private fun createSampleRocketDto(
        id: String,
        name: String,
    ): RocketDto =
        RocketDto(
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
