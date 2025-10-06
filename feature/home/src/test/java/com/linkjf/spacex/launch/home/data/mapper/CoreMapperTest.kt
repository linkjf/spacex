package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.CoreDto
import com.linkjf.spacex.launch.home.domain.model.Core
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CoreMapperTest {
    @Test
    fun `mapToDomain should map CoreDto to Core correctly`() {
        // Given
        val dto =
            CoreDto(
                core = "B1058",
                flight = 1,
                gridfins = true,
                legs = true,
                reused = false,
                landingAttempt = true,
                landingSuccess = true,
                landingType = "ASDS",
                landpad = "5e9e3032383ecb6bb234e7ca",
            )

        // When
        val result = CoreMapper.mapToDomain(dto)

        // Then
        assertEquals("B1058", result.core)
        assertEquals(1, result.flight)
        assertTrue(result.gridfins)
        assertTrue(result.legs)
        assertFalse(result.reused)
        assertTrue(result.landingAttempt)
        assertTrue(result.landingSuccess!!)
        assertEquals("ASDS", result.landingType)
        assertEquals("5e9e3032383ecb6bb234e7ca", result.landpad)
    }

    @Test
    fun `mapToDomain should handle null optional values correctly`() {
        // Given
        val dto =
            CoreDto(
                core = "B1058",
                flight = 1,
                gridfins = false,
                legs = false,
                reused = true,
                landingAttempt = false,
                landingSuccess = null,
                landingType = null,
                landpad = null,
            )

        // When
        val result = CoreMapper.mapToDomain(dto)

        // Then
        assertEquals("B1058", result.core)
        assertEquals(1, result.flight)
        assertFalse(result.gridfins)
        assertFalse(result.legs)
        assertTrue(result.reused)
        assertFalse(result.landingAttempt)
        assertNull(result.landingSuccess)
        assertNull(result.landingType)
        assertNull(result.landpad)
    }
}
