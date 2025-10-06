package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.*
import com.linkjf.spacex.launch.home.domain.model.Launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LaunchMapperTest {
    @Test
    fun `mapToDomain should map LaunchDto to Launch correctly`() {
        // Given
        val dto =
            LaunchDto(
                id = "5eb87cd9ffd86e000604b32a",
                name = "FalconSat",
                dateUtc = "2006-03-24T22:30:00.000Z",
                rocketId = "5e9d0d95eda69955f709d1eb",
                launchpadId = "5e9e3032383ecb6bb234e7ca",
                links =
                    LaunchLinksDto(
                        patch =
                            LaunchPatchDto(
                                small = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                                large = "https://images2.imgbox.com/f9/4a/ZboXReNb_o.png",
                            ),
                        webcast = "https://www.youtube.com/watch?v=0a_00nJ_Y88",
                        youtubeId = "0a_00nJ_Y88",
                        article = "https://www.space.com/2196-spacex-inaugural-falcon-1-rocket-lost-launch.html",
                        wikipedia = "https://en.wikipedia.org/wiki/DemoSat",
                    ),
                details = "Engine failure at 33 seconds and loss of vehicle",
                success = false,
                upcoming = false,
                flightNumber = 1,
                staticFireDateUtc = "2006-03-17T00:00:00.000Z",
                tbd = false,
                net = false,
                window = 0,
                payloads = listOf("5eb0e4b5b6c3bb0006eeb1e1"),
                capsules = emptyList(),
                ships = emptyList(),
                crew = emptyList(),
                cores =
                    listOf(
                        CoreDto(
                            core = "5e9e2897f3591813c3b2664",
                            flight = 1,
                            gridfins = false,
                            legs = false,
                            reused = false,
                            landingAttempt = false,
                            landingSuccess = null,
                            landingType = null,
                            landpad = null,
                        ),
                    ),
                fairings =
                    FairingsDto(
                        reused = false,
                        recoveryAttempt = false,
                        recovered = false,
                        ships = emptyList(),
                    ),
                autoUpdate = true,
                dateLocal = "2006-03-25T06:30:00+08:00",
                datePrecision = "hour",
                dateUnix = 1143239400L,
            )

        // When
        val result = LaunchMapper.mapToDomain(dto)

        // Then
        assertEquals("5eb87cd9ffd86e000604b32a", result.id)
        assertEquals("FalconSat", result.name)
        assertEquals("2006-03-24T22:30:00.000Z", result.dateUtc)
        assertEquals("5e9d0d95eda69955f709d1eb", result.rocketId)
        assertEquals("5e9e3032383ecb6bb234e7ca", result.launchpadId)

        // Test links mapping
        assertTrue(result.links != null)
        assertEquals("https://images2.imgbox.com/94/f2/NN6z45OK_o.png", result.links?.patch?.small)
        assertEquals("https://images2.imgbox.com/f9/4a/ZboXReNb_o.png", result.links?.patch?.large)
        assertEquals("https://www.youtube.com/watch?v=0a_00nJ_Y88", result.links?.webcast)
        assertEquals("0a_00nJ_Y88", result.links?.youtubeId)
        assertEquals("https://www.space.com/2196-spacex-inaugural-falcon-1-rocket-lost-launch.html", result.links?.article)
        assertEquals("https://en.wikipedia.org/wiki/DemoSat", result.links?.wikipedia)

        assertEquals("Engine failure at 33 seconds and loss of vehicle", result.details)
        assertFalse(result.success!!)
        assertFalse(result.upcoming)
        assertEquals(1, result.flightNumber)
        assertEquals("2006-03-17T00:00:00.000Z", result.staticFireDateUtc)
        assertFalse(result.tbd!!)
        assertFalse(result.net!!)
        assertEquals(0, result.window)

        // Test collections
        assertEquals(1, result.payloads?.size)
        assertEquals("5eb0e4b5b6c3bb0006eeb1e1", result.payloads?.get(0))
        assertTrue(result.capsules?.isEmpty()!!)
        assertTrue(result.ships?.isEmpty()!!)
        assertTrue(result.crew?.isEmpty()!!)

        // Test cores mapping
        assertEquals(1, result.cores?.size)
        assertEquals("5e9e2897f3591813c3b2664", result.cores?.get(0)?.core)
        assertEquals(1, result.cores?.get(0)?.flight)
        assertFalse(result.cores?.get(0)?.gridfins!!)
        assertFalse(result.cores?.get(0)?.legs!!)
        assertFalse(result.cores?.get(0)?.reused!!)
        assertFalse(result.cores?.get(0)?.landingAttempt!!)

        // Test fairings mapping
        assertTrue(result.fairings != null)
        assertFalse(result.fairings?.reused!!)
        assertFalse(result.fairings?.recoveryAttempt!!)
        assertFalse(result.fairings?.recovered!!)
        assertTrue(result.fairings?.ships?.isEmpty()!!)

        assertTrue(result.autoUpdate!!)
        assertEquals("2006-03-25T06:30:00+08:00", result.dateLocal)
        assertEquals("hour", result.datePrecision)
        assertEquals(1143239400L, result.dateUnix)

        // Rocket and launchpad should be null (populated separately)
        assertNull(result.rocket)
        assertNull(result.launchpad)
    }

    @Test
    fun `mapToDomain should handle null values correctly`() {
        // Given
        val dto =
            LaunchDto(
                id = "test-id",
                name = "Test Launch",
                dateUtc = "2023-01-01T00:00:00.000Z",
                rocketId = "test-rocket-id",
                launchpadId = "test-launchpad-id",
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

        // When
        val result = LaunchMapper.mapToDomain(dto)

        // Then
        assertEquals("test-id", result.id)
        assertEquals("Test Launch", result.name)
        assertEquals("2023-01-01T00:00:00.000Z", result.dateUtc)
        assertEquals("test-rocket-id", result.rocketId)
        assertEquals("test-launchpad-id", result.launchpadId)
        assertNull(result.links)
        assertNull(result.details)
        assertNull(result.success)
        assertTrue(result.upcoming)
        assertNull(result.flightNumber)
        assertNull(result.staticFireDateUtc)
        assertNull(result.tbd)
        assertNull(result.net)
        assertNull(result.window)
        assertNull(result.payloads)
        assertNull(result.capsules)
        assertNull(result.ships)
        assertNull(result.crew)
        assertNull(result.cores)
        assertNull(result.fairings)
        assertNull(result.autoUpdate)
        assertNull(result.dateLocal)
        assertNull(result.datePrecision)
        assertNull(result.dateUnix)
        assertNull(result.rocket)
        assertNull(result.launchpad)
    }

    @Test
    fun `mapToDomain should handle list mapping correctly`() {
        // Given
        val dtos =
            listOf(
                LaunchDto(
                    id = "launch1",
                    name = "Launch 1",
                    dateUtc = "2023-01-01T00:00:00.000Z",
                    rocketId = "rocket1",
                    launchpadId = "launchpad1",
                    links = null,
                    details = null,
                    success = true,
                    upcoming = false,
                    flightNumber = 1,
                    staticFireDateUtc = null,
                    tbd = false,
                    net = false,
                    window = 0,
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
                ),
                LaunchDto(
                    id = "launch2",
                    name = "Launch 2",
                    dateUtc = "2023-01-02T00:00:00.000Z",
                    rocketId = "rocket2",
                    launchpadId = "launchpad2",
                    links = null,
                    details = null,
                    success = false,
                    upcoming = true,
                    flightNumber = 2,
                    staticFireDateUtc = null,
                    tbd = true,
                    net = true,
                    window = 1,
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
                ),
            )

        // When
        val result = LaunchMapper.mapToDomain(dtos)

        // Then
        assertEquals(2, result.size)
        assertEquals("launch1", result[0].id)
        assertEquals("launch2", result[1].id)
        assertEquals("Launch 1", result[0].name)
        assertEquals("Launch 2", result[1].name)
        assertTrue(result[0].success!!)
        assertFalse(result[1].success!!)
        assertFalse(result[0].upcoming)
        assertTrue(result[1].upcoming)
        assertEquals(1, result[0].flightNumber)
        assertEquals(2, result[1].flightNumber)
    }
}
