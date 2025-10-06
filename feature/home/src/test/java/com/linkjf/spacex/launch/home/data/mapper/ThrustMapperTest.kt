package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.ThrustDto
import com.linkjf.spacex.launch.home.domain.model.Thrust
import org.junit.Assert.assertEquals
import org.junit.Test

class ThrustMapperTest {
    @Test
    fun `mapToDomain should map ThrustDto to Thrust correctly`() {
        // Given
        val dto =
            ThrustDto(
                kN = 845,
                lbf = 190000,
            )

        // When
        val result = ThrustMapper.mapToDomain(dto)

        // Then
        assertEquals(845, result.kN)
        assertEquals(190000, result.lbf)
    }

    @Test
    fun `mapToDomain should handle zero values correctly`() {
        // Given
        val dto =
            ThrustDto(
                kN = 0,
                lbf = 0,
            )

        // When
        val result = ThrustMapper.mapToDomain(dto)

        // Then
        assertEquals(0, result.kN)
        assertEquals(0, result.lbf)
    }
}
