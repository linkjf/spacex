package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.MassDto
import com.linkjf.spacex.launch.home.domain.model.Mass
import org.junit.Assert.assertEquals
import org.junit.Test

class MassMapperTest {
    @Test
    fun `mapToDomain should map MassDto to Mass correctly`() {
        // Given
        val dto =
            MassDto(
                kg = 549054,
                lb = 1207920,
            )

        // When
        val result = MassMapper.mapToDomain(dto)

        // Then
        assertEquals(549054, result.kg)
        assertEquals(1207920, result.lb)
    }

    @Test
    fun `mapToDomain should handle zero values correctly`() {
        // Given
        val dto =
            MassDto(
                kg = 0,
                lb = 0,
            )

        // When
        val result = MassMapper.mapToDomain(dto)

        // Then
        assertEquals(0, result.kg)
        assertEquals(0, result.lb)
    }
}
