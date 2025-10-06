package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.FairingsDto
import com.linkjf.spacex.launch.home.domain.model.Fairings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FairingsMapperTest {
    @Test
    fun `mapToDomain should map FairingsDto to Fairings correctly`() {
        // Given
        val dto =
            FairingsDto(
                reused = true,
                recoveryAttempt = true,
                recovered = true,
                ships = listOf("5ea6ed2e080df4000697c906", "5ea6ed2f080df4000697c907"),
            )

        // When
        val result = FairingsMapper.mapToDomain(dto)

        // Then
        assertTrue(result.reused!!)
        assertTrue(result.recoveryAttempt!!)
        assertTrue(result.recovered!!)
        assertEquals(2, result.ships.size)
        assertEquals("5ea6ed2e080df4000697c906", result.ships[0])
        assertEquals("5ea6ed2f080df4000697c907", result.ships[1])
    }

    @Test
    fun `mapToDomain should handle null optional values correctly`() {
        // Given
        val dto =
            FairingsDto(
                reused = null,
                recoveryAttempt = null,
                recovered = null,
                ships = emptyList(),
            )

        // When
        val result = FairingsMapper.mapToDomain(dto)

        // Then
        assertNull(result.reused)
        assertNull(result.recoveryAttempt)
        assertNull(result.recovered)
        assertTrue(result.ships.isEmpty())
    }

    @Test
    fun `mapToDomain should handle false values correctly`() {
        // Given
        val dto =
            FairingsDto(
                reused = false,
                recoveryAttempt = false,
                recovered = false,
                ships = listOf("5ea6ed2e080df4000697c906"),
            )

        // When
        val result = FairingsMapper.mapToDomain(dto)

        // Then
        assertFalse(result.reused!!)
        assertFalse(result.recoveryAttempt!!)
        assertFalse(result.recovered!!)
        assertEquals(1, result.ships.size)
        assertEquals("5ea6ed2e080df4000697c906", result.ships[0])
    }
}
