package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.LaunchpadDto
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LaunchpadMapperTest {
    @Test
    fun `mapToDomain should map LaunchpadDto to Launchpad correctly`() {
        // Given
        val dto =
            LaunchpadDto(
                id = "5e9e3032383ecb6bb234e7ca",
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
                rockets = listOf("5e9d0d95eda69955f709d1eb", "5e9d0d95eda69973a809d1ec"),
                launches = listOf("5eb87cdeffd86e000604b32a", "5eb87cdfffd86e000604b32b"),
                details = "SpaceX historic launch site that was used for the Apollo and Space Shuttle programs",
            )

        // When
        val result = LaunchpadMapper.mapToDomain(dto)

        // Then
        assertEquals("5e9e3032383ecb6bb234e7ca", result.id)
        assertEquals("KSC LC 39A", result.name)
        assertEquals("Kennedy Space Center Historic Launch Complex 39A", result.fullName)
        assertEquals("active", result.status)
        assertEquals("Cape Canaveral", result.locality)
        assertEquals("Florida", result.region)
        assertEquals("America/New_York", result.timezone)
        assertEquals(28.6080585, result.latitude, 0.0000001)
        assertEquals(-80.6039558, result.longitude, 0.0000001)
        assertEquals(127, result.launchAttempts)
        assertEquals(125, result.launchSuccesses)
        assertEquals(2, result.rockets.size)
        assertEquals("5e9d0d95eda69955f709d1eb", result.rockets[0])
        assertEquals("5e9d0d95eda69973a809d1ec", result.rockets[1])
        assertEquals(2, result.launches.size)
        assertEquals("5eb87cdeffd86e000604b32a", result.launches[0])
        assertEquals("5eb87cdfffd86e000604b32b", result.launches[1])
        assertEquals("SpaceX historic launch site that was used for the Apollo and Space Shuttle programs", result.details)
    }

    @Test
    fun `mapToDomain should handle empty lists correctly`() {
        // Given
        val dto =
            LaunchpadDto(
                id = "test-id",
                name = "Test Launchpad",
                fullName = "Test Launchpad Full Name",
                status = "inactive",
                locality = "Test Locality",
                region = "Test Region",
                timezone = "UTC",
                latitude = 0.0,
                longitude = 0.0,
                launchAttempts = 0,
                launchSuccesses = 0,
                rockets = emptyList(),
                launches = emptyList(),
                details = "Test details",
            )

        // When
        val result = LaunchpadMapper.mapToDomain(dto)

        // Then
        assertEquals("test-id", result.id)
        assertEquals("Test Launchpad", result.name)
        assertTrue(result.rockets.isEmpty())
        assertTrue(result.launches.isEmpty())
    }

    @Test
    fun `mapToDomain should handle list mapping correctly`() {
        // Given
        val dtos =
            listOf(
                LaunchpadDto(
                    id = "pad1",
                    name = "Pad 1",
                    fullName = "Pad 1 Full",
                    status = "active",
                    locality = "Locality 1",
                    region = "Region 1",
                    timezone = "UTC",
                    latitude = 1.0,
                    longitude = 1.0,
                    launchAttempts = 10,
                    launchSuccesses = 9,
                    rockets = emptyList(),
                    launches = emptyList(),
                    details = "Details 1",
                ),
                LaunchpadDto(
                    id = "pad2",
                    name = "Pad 2",
                    fullName = "Pad 2 Full",
                    status = "inactive",
                    locality = "Locality 2",
                    region = "Region 2",
                    timezone = "UTC",
                    latitude = 2.0,
                    longitude = 2.0,
                    launchAttempts = 5,
                    launchSuccesses = 4,
                    rockets = emptyList(),
                    launches = emptyList(),
                    details = "Details 2",
                ),
            )

        // When
        val result = LaunchpadMapper.mapToDomain(dtos)

        // Then
        assertEquals(2, result.size)
        assertEquals("pad1", result[0].id)
        assertEquals("pad2", result[1].id)
        assertEquals("Pad 1", result[0].name)
        assertEquals("Pad 2", result[1].name)
    }
}
