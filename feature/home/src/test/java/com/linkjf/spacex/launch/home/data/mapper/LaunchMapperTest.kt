package com.linkjf.spacex.launch.home.data.mapper

import com.linkjf.spacex.launch.home.data.remote.dto.LaunchDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLinksDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchPatchDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LaunchMapperTest {

    @Test
    fun `mapToDomain should map LaunchDto to Launch correctly`() {
        val launchDto = LaunchDto(
            id = "launch_1",
            name = "Falcon 9 Test Flight",
            dateUtc = "2024-01-15T14:30:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links = LaunchLinksDto(
                patch = LaunchPatchDto(
                    small = "https://example.com/patch_small.png",
                    large = "https://example.com/patch_large.png"
                ),
                webcast = "https://youtube.com/watch?v=dQw4w9WgXcQ"
            ),
            details = "Test flight details",
            success = true,
            upcoming = true
        )

        val result = LaunchMapper.mapToDomain(launchDto)

        assertEquals("launch_1", result.id)
        assertEquals("Falcon 9 Test Flight", result.name)
        assertEquals("2024-01-15T14:30:00.000Z", result.dateUtc)
        assertEquals("falcon9", result.rocketId)
        assertEquals("ksc_lc_39a", result.launchpadId)
        assertEquals("Test flight details", result.details)
        assertEquals(true, result.success)
        assertEquals(true, result.upcoming)
        assertEquals("https://example.com/patch_small.png", result.links?.patch?.small)
        assertEquals("https://example.com/patch_large.png", result.links?.patch?.large)
        assertEquals("https://youtube.com/watch?v=dQw4w9WgXcQ", result.links?.webcast)
    }

    @Test
    fun `mapToDomain should handle null success value`() {
        val launchDto = LaunchDto(
            id = "launch_2",
            name = "Starlink Group 2-38",
            dateUtc = "2024-01-20T10:00:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links = LaunchLinksDto(
                patch = LaunchPatchDto(
                    small = "https://example.com/patch.png",
                    large = "https://example.com/patch_large.png"
                ),
                webcast = "https://youtube.com/watch?v=test"
            ),
            details = null,
            success = null,
            upcoming = true
        )

        val result = LaunchMapper.mapToDomain(launchDto)

        assertEquals("launch_2", result.id)
        assertEquals("Starlink Group 2-38", result.name)
        assertEquals("2024-01-20T10:00:00.000Z", result.dateUtc)
        assertEquals("falcon9", result.rocketId)
        assertEquals("ksc_lc_39a", result.launchpadId)
        assertNull(result.details)
        assertNull(result.success)
        assertEquals(true, result.upcoming)
        assertEquals("https://example.com/patch.png", result.links?.patch?.small)
        assertEquals("https://youtube.com/watch?v=test", result.links?.webcast)
    }

    @Test
    fun `mapToDomain should handle null patch image`() {
        val launchDto = LaunchDto(
            id = "launch_3",
            name = "Test Launch",
            dateUtc = "2024-01-25T12:00:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links = LaunchLinksDto(
                patch = null,
                webcast = "https://youtube.com/watch?v=test2"
            ),
            details = "Test details",
            success = false,
            upcoming = false
        )

        val result = LaunchMapper.mapToDomain(launchDto)

        assertEquals("launch_3", result.id)
        assertEquals("Test Launch", result.name)
        assertEquals("2024-01-25T12:00:00.000Z", result.dateUtc)
        assertEquals("falcon9", result.rocketId)
        assertEquals("ksc_lc_39a", result.launchpadId)
        assertEquals("Test details", result.details)
        assertEquals(false, result.success)
        assertEquals(false, result.upcoming)
        assertNull(result.links?.patch)
        assertEquals("https://youtube.com/watch?v=test2", result.links?.webcast)
    }

    @Test
    fun `mapToDomain should handle null webcast URL`() {
        val launchDto = LaunchDto(
            id = "launch_4",
            name = "No Webcast Launch",
            dateUtc = "2024-01-30T08:00:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links = LaunchLinksDto(
                patch = LaunchPatchDto(
                    small = "https://example.com/patch.png",
                    large = "https://example.com/patch_large.png"
                ),
                webcast = null
            ),
            details = "No webcast details",
            success = true,
            upcoming = true
        )

        val result = LaunchMapper.mapToDomain(launchDto)

        assertEquals("launch_4", result.id)
        assertEquals("No Webcast Launch", result.name)
        assertEquals("2024-01-30T08:00:00.000Z", result.dateUtc)
        assertEquals("falcon9", result.rocketId)
        assertEquals("ksc_lc_39a", result.launchpadId)
        assertEquals("No webcast details", result.details)
        assertEquals(true, result.success)
        assertEquals(true, result.upcoming)
        assertEquals("https://example.com/patch.png", result.links?.patch?.small)
        assertNull(result.links?.webcast)
    }

    @Test
    fun `mapToDomain should handle all null values`() {
        val launchDto = LaunchDto(
            id = "launch_5",
            name = "Minimal Launch",
            dateUtc = "2024-02-01T16:00:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links = null,
            details = null,
            success = null,
            upcoming = false
        )

        val result = LaunchMapper.mapToDomain(launchDto)

        assertEquals("launch_5", result.id)
        assertEquals("Minimal Launch", result.name)
        assertEquals("2024-02-01T16:00:00.000Z", result.dateUtc)
        assertEquals("falcon9", result.rocketId)
        assertEquals("ksc_lc_39a", result.launchpadId)
        assertNull(result.links)
        assertNull(result.details)
        assertNull(result.success)
        assertEquals(false, result.upcoming)
    }

    @Test
    fun `mapToDomain should handle list of LaunchDtos`() {
        val launchDtos = listOf(
            LaunchDto(
                id = "launch_1",
                name = "Launch 1",
                dateUtc = "2024-01-15T14:30:00.000Z",
                rocketId = "falcon9",
                launchpadId = "ksc_lc_39a",
                links = null,
                details = null,
                success = true,
                upcoming = true
            ),
            LaunchDto(
                id = "launch_2",
                name = "Launch 2",
                dateUtc = "2024-01-20T10:00:00.000Z",
                rocketId = "falcon9",
                launchpadId = "ksc_lc_39a",
                links = null,
                details = null,
                success = false,
                upcoming = false
            )
        )

        val result = LaunchMapper.mapToDomain(launchDtos)

        assertEquals(2, result.size)
        assertEquals("Launch 1", result[0].name)
        assertEquals("Launch 2", result[1].name)
        assertEquals(true, result[0].success)
        assertEquals(false, result[1].success)
    }

    @Test
    fun `mapToDomain should handle LaunchLinks with all fields`() {
        val launchDto = LaunchDto(
            id = "launch_6",
            name = "Complete Launch",
            dateUtc = "2024-02-05T12:00:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links = LaunchLinksDto(
                patch = LaunchPatchDto(
                    small = "https://example.com/small.png",
                    large = "https://example.com/large.png"
                ),
                webcast = "https://youtube.com/watch?v=webcast",
                youtubeId = "youtube123",
                article = "https://example.com/article",
                wikipedia = "https://wikipedia.com/launch"
            ),
            details = "Complete launch details",
            success = true,
            upcoming = true
        )

        val result = LaunchMapper.mapToDomain(launchDto)

        assertEquals("https://example.com/small.png", result.links?.patch?.small)
        assertEquals("https://example.com/large.png", result.links?.patch?.large)
        assertEquals("https://youtube.com/watch?v=webcast", result.links?.webcast)
        assertEquals("youtube123", result.links?.youtubeId)
        assertEquals("https://example.com/article", result.links?.article)
        assertEquals("https://wikipedia.com/launch", result.links?.wikipedia)
    }
}
