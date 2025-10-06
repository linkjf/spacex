package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.IspDto
import com.linkjf.spacex.launch.home.domain.model.Isp
import org.junit.Assert.assertEquals
import org.junit.Test

class IspMapperTest {
    @Test
    fun `mapToDomain should map IspDto to Isp correctly`() {
        // Given
        val dto =
            IspDto(
                seaLevel = 282,
                vacuum = 311,
            )

        // When
        val result = IspMapper.mapToDomain(dto)

        // Then
        assertEquals(282, result.seaLevel)
        assertEquals(311, result.vacuum)
    }

    @Test
    fun `mapToDomain should handle zero values correctly`() {
        // Given
        val dto =
            IspDto(
                seaLevel = 0,
                vacuum = 0,
            )

        // When
        val result = IspMapper.mapToDomain(dto)

        // Then
        assertEquals(0, result.seaLevel)
        assertEquals(0, result.vacuum)
    }
}
