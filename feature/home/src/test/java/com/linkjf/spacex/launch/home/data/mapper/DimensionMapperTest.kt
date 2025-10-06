package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.DimensionDto
import com.linkjf.spacex.launch.home.domain.model.Dimension
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DimensionMapperTest {
    @Test
    fun `mapToDomain should map DimensionDto to Dimension correctly`() {
        // Given
        val dto =
            DimensionDto(
                meters = 70.0,
                feet = 229.6,
            )

        // When
        val result = DimensionMapper.mapToDomain(dto)

        // Then
        assertEquals(70.0, result.meters!!, 0.01)
        assertEquals(229.6, result.feet!!, 0.01)
    }

    @Test
    fun `mapToDomain should handle null values correctly`() {
        // Given
        val dto =
            DimensionDto(
                meters = null,
                feet = null,
            )

        // When
        val result = DimensionMapper.mapToDomain(dto)

        // Then
        assertNull(result.meters)
        assertNull(result.feet)
    }

    @Test
    fun `mapToDomain should handle partial null values correctly`() {
        // Given
        val dto =
            DimensionDto(
                meters = 70.0,
                feet = null,
            )

        // When
        val result = DimensionMapper.mapToDomain(dto)

        // Then
        assertEquals(70.0, result.meters!!, 0.01)
        assertNull(result.feet)
    }
}
