package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.*
import com.linkjf.spacex.launch.home.domain.model.Rocket
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class RocketMapperTest {
    @Test
    fun `mapToDomain should map RocketDto to Rocket correctly`() {
        // Given
        val dto =
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
                payloadWeights =
                    listOf(
                        PayloadWeightDto(
                            id = "leo",
                            name = "Low Earth Orbit",
                            kg = 22800,
                            lb = 50300,
                        ),
                    ),
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
                flickrImages =
                    listOf(
                        "https://imgur.com/DaCfMsj.jpg",
                        "https://imgur.com/azYafd8.jpg",
                    ),
                wikipedia = "https://en.wikipedia.org/wiki/Falcon_9",
                description = "Falcon 9 is a two-stage rocket designed and manufactured by SpaceX for the reliable and safe transport of people and payloads into Earth orbit and beyond.",
            )

        // When
        val result = RocketMapper.mapToDomain(dto)

        // Then
        assertNotNull("Rocket should not be null", result)
        assertEquals("falcon9", result.id)
        assertEquals("Falcon 9", result.name)
        assertEquals("rocket", result.type)
        assertEquals(true, result.active)
        assertEquals(2, result.stages)
        assertEquals(0, result.boosters)
        assertEquals(6700000L, result.costPerLaunch)
        assertEquals(98.0, result.successRatePct, 0.01)
        assertEquals("2010-06-04", result.firstFlight)
        assertEquals("Republic of the Marshall Islands", result.country)
        assertEquals("SpaceX", result.company)

        // Test dimension mapping
        assertNotNull("Height should not be null", result.height)
        assertEquals(70.0, result.height.meters!!, 0.01)
        assertEquals(229.6, result.height.feet!!, 0.01)

        assertNotNull("Diameter should not be null", result.diameter)
        assertEquals(3.7, result.diameter.meters!!, 0.01)
        assertEquals(12.0, result.diameter.feet!!, 0.01)

        // Test mass mapping
        assertNotNull("Mass should not be null", result.mass)
        assertEquals(549054, result.mass.kg)
        assertEquals(1207920, result.mass.lb)

        // Test payload weights mapping
        assertNotNull("Payload weights should not be null", result.payloadWeights)
        assertEquals(1, result.payloadWeights.size)
        val payloadWeight = result.payloadWeights[0]
        assertEquals("leo", payloadWeight.id)
        assertEquals("Low Earth Orbit", payloadWeight.name)
        assertEquals(22800, payloadWeight.kg)
        assertEquals(50300, payloadWeight.lb)

        // Test stage mapping
        assertNotNull("First stage should not be null", result.firstStage)
        assertEquals(true, result.firstStage.reusable)
        assertEquals(9, result.firstStage.engines)
        assertEquals(385.0, result.firstStage.fuelAmountTons, 0.01)
        assertEquals(162, result.firstStage.burnTimeSec)

        assertNotNull("Second stage should not be null", result.secondStage)
        assertEquals(false, result.secondStage.reusable)
        assertEquals(1, result.secondStage.engines)
        assertEquals(90.0, result.secondStage.fuelAmountTons, 0.01)
        assertEquals(397, result.secondStage.burnTimeSec)

        // Test engine mapping
        assertNotNull("Engines should not be null", result.engines)
        assertEquals(9, result.engines.number)
        assertEquals("merlin", result.engines.type)
        assertEquals("1D+", result.engines.version)
        assertEquals("octaweb", result.engines.layout)
        assertEquals(2, result.engines.engineLossMax)
        assertEquals("liquid oxygen", result.engines.propellant1)
        assertEquals("RP-1 kerosene", result.engines.propellant2)
        assertEquals(180.1, result.engines.thrustToWeight, 0.01)

        // Test landing legs mapping
        assertNotNull("Landing legs should not be null", result.landingLegs)
        assertEquals(4, result.landingLegs.number)
        assertEquals("carbon fiber", result.landingLegs.material)

        // Test collections
        assertNotNull("Flickr images should not be null", result.flickrImages)
        assertEquals(2, result.flickrImages.size)
        assertEquals("https://imgur.com/DaCfMsj.jpg", result.flickrImages[0])
        assertEquals("https://imgur.com/azYafd8.jpg", result.flickrImages[1])

        assertEquals("https://en.wikipedia.org/wiki/Falcon_9", result.wikipedia)
        assertTrue("Description should contain expected text", result.description.contains("Falcon 9 is a two-stage rocket"))
    }

    @Test
    fun `mapToDomain should handle list mapping correctly`() {
        // Given
        val dtos =
            listOf(
                RocketDto(
                    id = "rocket1",
                    name = "Rocket 1",
                    type = "rocket",
                    active = true,
                    stages = 2,
                    boosters = 0,
                    costPerLaunch = 1000000,
                    successRatePct = 95.0,
                    firstFlight = "2020-01-01",
                    country = "USA",
                    company = "SpaceX",
                    height = DimensionDto(meters = 50.0, feet = 164.0),
                    diameter = DimensionDto(meters = 3.0, feet = 10.0),
                    mass = MassDto(kg = 100000, lb = 220000),
                    payloadWeights = emptyList(),
                    firstStage =
                        StageDto(
                            reusable = true,
                            engines = 9,
                            fuelAmountTons = 200.0,
                            burnTimeSec = 100,
                            thrustSeaLevel = ThrustDto(kN = 1000, lbf = 225000),
                            thrustVacuum = ThrustDto(kN = 1100, lbf = 247500),
                        ),
                    secondStage =
                        StageDto(
                            reusable = false,
                            engines = 1,
                            fuelAmountTons = 50.0,
                            burnTimeSec = 200,
                            thrustSeaLevel = ThrustDto(kN = 100, lbf = 22500),
                            thrustVacuum = ThrustDto(kN = 100, lbf = 22500),
                        ),
                    engines =
                        EngineDto(
                            isp = IspDto(seaLevel = 250, vacuum = 300),
                            thrustSeaLevel = ThrustDto(kN = 100, lbf = 22500),
                            thrustVacuum = ThrustDto(kN = 110, lbf = 24750),
                            number = 9,
                            type = "test",
                            version = "1.0",
                            layout = "test",
                            engineLossMax = 1,
                            propellant1 = "test1",
                            propellant2 = "test2",
                            thrustToWeight = 100.0,
                        ),
                    landingLegs = LandingLegsDto(number = 4, material = "steel"),
                    flickrImages = emptyList(),
                    wikipedia = "https://example.com",
                    description = "Test rocket",
                ),
                RocketDto(
                    id = "rocket2",
                    name = "Rocket 2",
                    type = "rocket",
                    active = false,
                    stages = 1,
                    boosters = 2,
                    costPerLaunch = 2000000,
                    successRatePct = 90.0,
                    firstFlight = "2021-01-01",
                    country = "USA",
                    company = "SpaceX",
                    height = DimensionDto(meters = 60.0, feet = 197.0),
                    diameter = DimensionDto(meters = 4.0, feet = 13.0),
                    mass = MassDto(kg = 200000, lb = 440000),
                    payloadWeights = emptyList(),
                    firstStage =
                        StageDto(
                            reusable = false,
                            engines = 1,
                            fuelAmountTons = 100.0,
                            burnTimeSec = 50,
                            thrustSeaLevel = ThrustDto(kN = 500, lbf = 112500),
                            thrustVacuum = ThrustDto(kN = 550, lbf = 123750),
                        ),
                    secondStage =
                        StageDto(
                            reusable = false,
                            engines = 1,
                            fuelAmountTons = 25.0,
                            burnTimeSec = 100,
                            thrustSeaLevel = ThrustDto(kN = 50, lbf = 11250),
                            thrustVacuum = ThrustDto(kN = 50, lbf = 11250),
                        ),
                    engines =
                        EngineDto(
                            isp = IspDto(seaLevel = 200, vacuum = 250),
                            thrustSeaLevel = ThrustDto(kN = 50, lbf = 11250),
                            thrustVacuum = ThrustDto(kN = 55, lbf = 12375),
                            number = 1,
                            type = "test2",
                            version = "2.0",
                            layout = "test2",
                            engineLossMax = 0,
                            propellant1 = "test3",
                            propellant2 = "test4",
                            thrustToWeight = 50.0,
                        ),
                    landingLegs = LandingLegsDto(number = 3, material = "aluminum"),
                    flickrImages = emptyList(),
                    wikipedia = "https://example2.com",
                    description = "Test rocket 2",
                ),
            )

        // When
        val result = RocketMapper.mapToDomain(dtos)

        // Then
        assertEquals(2, result.size)
        assertEquals("rocket1", result[0].id)
        assertEquals("rocket2", result[1].id)
        assertEquals("Rocket 1", result[0].name)
        assertEquals("Rocket 2", result[1].name)
        assertEquals(true, result[0].active)
        assertEquals(false, result[1].active)
        assertEquals(2, result[0].stages)
        assertEquals(1, result[1].stages)
        assertEquals(0, result[0].boosters)
        assertEquals(2, result[1].boosters)
    }
}
