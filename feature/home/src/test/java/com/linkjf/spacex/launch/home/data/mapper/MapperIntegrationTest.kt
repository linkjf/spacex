package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.*
import com.linkjf.spacex.launch.home.domain.model.Launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MapperIntegrationTest {
    @Test
    fun `test all mappers work together correctly`() {
        // Given - Create a comprehensive LaunchDto with all nested objects
        val launchDto =
            LaunchDto(
                id = "test-launch-id",
                name = "Test Launch",
                dateUtc = "2023-01-01T00:00:00.000Z",
                rocketId = "test-rocket-id",
                launchpadId = "test-launchpad-id",
                links =
                    LaunchLinksDto(
                        patch =
                            LaunchPatchDto(
                                small = "https://example.com/small.png",
                                large = "https://example.com/large.png",
                            ),
                        webcast = "https://youtube.com/watch?v=test",
                        youtubeId = "test",
                        article = "https://example.com/article",
                        wikipedia = "https://wikipedia.org/test",
                    ),
                details = "Test launch details",
                success = true,
                upcoming = false,
                flightNumber = 1,
                staticFireDateUtc = "2022-12-31T00:00:00.000Z",
                tbd = false,
                net = false,
                window = 0,
                payloads = listOf("payload1", "payload2"),
                capsules = listOf("capsule1"),
                ships = listOf("ship1", "ship2"),
                crew = emptyList(),
                cores =
                    listOf(
                        CoreDto(
                            core = "core1",
                            flight = 1,
                            gridfins = true,
                            legs = true,
                            reused = false,
                            landingAttempt = true,
                            landingSuccess = true,
                            landingType = "ASDS",
                            landpad = "landpad1",
                        ),
                    ),
                fairings =
                    FairingsDto(
                        reused = false,
                        recoveryAttempt = true,
                        recovered = false,
                        ships = listOf("ship3"),
                    ),
                autoUpdate = true,
                dateLocal = "2023-01-01T00:00:00+00:00",
                datePrecision = "hour",
                dateUnix = 1672531200L,
            )

        // When - Map to domain model
        val result = LaunchMapper.mapToDomain(launchDto)

        // Then - Verify all mappings work correctly
        assertNotNull("Launch should not be null", result)
        assertEquals("test-launch-id", result.id)
        assertEquals("Test Launch", result.name)
        assertEquals("2023-01-01T00:00:00.000Z", result.dateUtc)
        assertEquals("test-rocket-id", result.rocketId)
        assertEquals("test-launchpad-id", result.launchpadId)

        // Test links mapping
        assertNotNull("Links should not be null", result.links)
        assertNotNull("Patch should not be null", result.links?.patch)
        assertEquals("https://example.com/small.png", result.links?.patch?.small)
        assertEquals("https://example.com/large.png", result.links?.patch?.large)
        assertEquals("https://youtube.com/watch?v=test", result.links?.webcast)
        assertEquals("test", result.links?.youtubeId)
        assertEquals("https://example.com/article", result.links?.article)
        assertEquals("https://wikipedia.org/test", result.links?.wikipedia)

        // Test basic fields
        assertEquals("Test launch details", result.details)
        assertEquals(true, result.success)
        assertEquals(false, result.upcoming)
        assertEquals(1, result.flightNumber)
        assertEquals("2022-12-31T00:00:00.000Z", result.staticFireDateUtc)
        assertEquals(false, result.tbd)
        assertEquals(false, result.net)
        assertEquals(0, result.window)

        // Test collections
        assertNotNull("Payloads should not be null", result.payloads)
        assertEquals(2, result.payloads?.size)
        assertEquals("payload1", result.payloads?.get(0))
        assertEquals("payload2", result.payloads?.get(1))

        assertNotNull("Capsules should not be null", result.capsules)
        assertEquals(1, result.capsules?.size)
        assertEquals("capsule1", result.capsules?.get(0))

        assertNotNull("Ships should not be null", result.ships)
        assertEquals(2, result.ships?.size)
        assertEquals("ship1", result.ships?.get(0))
        assertEquals("ship2", result.ships?.get(1))

        assertNotNull("Crew should not be null", result.crew)
        assertEquals(0, result.crew?.size)

        // Test cores mapping
        assertNotNull("Cores should not be null", result.cores)
        assertEquals(1, result.cores?.size)
        val core = result.cores?.get(0)
        assertNotNull("Core should not be null", core)
        assertEquals("core1", core?.core)
        assertEquals(1, core?.flight)
        assertEquals(true, core?.gridfins)
        assertEquals(true, core?.legs)
        assertEquals(false, core?.reused)
        assertEquals(true, core?.landingAttempt)
        assertEquals(true, core?.landingSuccess)
        assertEquals("ASDS", core?.landingType)
        assertEquals("landpad1", core?.landpad)

        // Test fairings mapping
        assertNotNull("Fairings should not be null", result.fairings)
        assertEquals(false, result.fairings?.reused)
        assertEquals(true, result.fairings?.recoveryAttempt)
        assertEquals(false, result.fairings?.recovered)
        assertNotNull("Fairings ships should not be null", result.fairings?.ships)
        assertEquals(1, result.fairings?.ships?.size)
        assertEquals("ship3", result.fairings?.ships?.get(0))

        // Test additional fields
        assertEquals(true, result.autoUpdate)
        assertEquals("2023-01-01T00:00:00+00:00", result.dateLocal)
        assertEquals("hour", result.datePrecision)
        assertEquals(1672531200L, result.dateUnix)

        // Rocket and launchpad should be null (populated separately)
        assertEquals(null, result.rocket)
        assertEquals(null, result.launchpad)
    }

    @Test
    fun `test mapper handles null values gracefully`() {
        // Given - Create a minimal LaunchDto with null values
        val launchDto =
            LaunchDto(
                id = "minimal-launch",
                name = "Minimal Launch",
                dateUtc = "2023-01-01T00:00:00.000Z",
                rocketId = "rocket-id",
                launchpadId = "launchpad-id",
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

        // When - Map to domain model
        val result = LaunchMapper.mapToDomain(launchDto)

        // Then - Verify null handling
        assertNotNull("Launch should not be null", result)
        assertEquals("minimal-launch", result.id)
        assertEquals("Minimal Launch", result.name)
        assertEquals("2023-01-01T00:00:00.000Z", result.dateUtc)
        assertEquals("rocket-id", result.rocketId)
        assertEquals("launchpad-id", result.launchpadId)

        // All optional fields should be null
        assertEquals(null, result.links)
        assertEquals(null, result.details)
        assertEquals(null, result.success)
        assertEquals(true, result.upcoming)
        assertEquals(null, result.flightNumber)
        assertEquals(null, result.staticFireDateUtc)
        assertEquals(null, result.tbd)
        assertEquals(null, result.net)
        assertEquals(null, result.window)
        assertEquals(null, result.payloads)
        assertEquals(null, result.capsules)
        assertEquals(null, result.ships)
        assertEquals(null, result.crew)
        assertEquals(null, result.cores)
        assertEquals(null, result.fairings)
        assertEquals(null, result.autoUpdate)
        assertEquals(null, result.dateLocal)
        assertEquals(null, result.datePrecision)
        assertEquals(null, result.dateUnix)
        assertEquals(null, result.rocket)
        assertEquals(null, result.launchpad)
    }
}
