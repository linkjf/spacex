package com.linkjf.spacex.launch.home.data

import com.linkjf.spacex.launch.home.data.remote.dto.LaunchDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLinksDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchPatchDto
import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.LaunchLinks
import com.linkjf.spacex.launch.home.domain.model.LaunchPatch

object TestFixtures {
    fun createMockLaunchDtos(): List<LaunchDto> =
        listOf(
            LaunchDto(
                id = "launch_1",
                name = "Falcon 9 Test Flight",
                dateUtc = "2024-01-15T14:30:00.000Z",
                rocketId = "falcon9",
                launchpadId = "ksc_lc_39a",
                links =
                    LaunchLinksDto(
                        patch =
                            LaunchPatchDto(
                                small = "https://example.com/patch_small.png",
                                large = "https://example.com/patch_large.png",
                            ),
                        webcast = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                    ),
                details = "Test flight details",
                success = true,
                upcoming = true,
            ),
            LaunchDto(
                id = "launch_2",
                name = "Starlink Group 2-38",
                dateUtc = "2024-01-20T10:00:00.000Z",
                rocketId = "falcon9",
                launchpadId = "ksc_lc_39a",
                links =
                    LaunchLinksDto(
                        patch =
                            LaunchPatchDto(
                                small = "https://example.com/starlink_patch.png",
                                large = "https://example.com/starlink_patch_large.png",
                            ),
                        webcast = null,
                    ),
                details = "Starlink mission",
                success = null,
                upcoming = true,
            ),
            LaunchDto(
                id = "launch_3",
                name = "Falcon Heavy Demo",
                dateUtc = "2024-01-25T16:00:00.000Z",
                rocketId = "falconheavy",
                launchpadId = "ksc_lc_39a",
                links =
                    LaunchLinksDto(
                        patch =
                            LaunchPatchDto(
                                small = "https://example.com/falcon_heavy_patch.png",
                                large = "https://example.com/falcon_heavy_patch_large.png",
                            ),
                        webcast = "https://youtube.com/watch?v=Lk4zQ2wP-Nc",
                    ),
                details = "Falcon Heavy demonstration",
                success = false,
                upcoming = false,
            ),
        )

    fun createMockLaunchDtosWithNulls(): List<LaunchDto> =
        listOf(
            LaunchDto(
                id = "launch_null",
                name = "Test Launch",
                dateUtc = "2024-01-15T14:30:00.000Z",
                rocketId = "falcon9",
                launchpadId = "ksc_lc_39a",
                links =
                    LaunchLinksDto(
                        patch = null,
                        webcast = null,
                    ),
                details = null,
                success = null,
                upcoming = true,
            ),
        )

    fun createMockLaunches(): List<Launch> =
        listOf(
            Launch(
                id = "launch_1",
                name = "Falcon 9 Test Flight",
                dateUtc = "2024-01-15T14:30:00.000Z",
                rocketId = "falcon9",
                launchpadId = "ksc_lc_39a",
                links =
                    LaunchLinks(
                        patch =
                            LaunchPatch(
                                small = "https://example.com/patch_small.png",
                                large = "https://example.com/patch_large.png",
                            ),
                        webcast = "https://youtube.com/watch?v=dQw4w9WgXcQ",
                    ),
                details = "Test flight details",
                success = true,
                upcoming = true,
            ),
            Launch(
                id = "launch_2",
                name = "Starlink Group 2-38",
                dateUtc = "2024-01-20T10:00:00.000Z",
                rocketId = "falcon9",
                launchpadId = "ksc_lc_39a",
                links =
                    LaunchLinks(
                        patch =
                            LaunchPatch(
                                small = "https://example.com/starlink_patch.png",
                                large = "https://example.com/starlink_patch_large.png",
                            ),
                        webcast = null,
                    ),
                details = "Starlink mission",
                success = null,
                upcoming = true,
            ),
            Launch(
                id = "launch_3",
                name = "Falcon Heavy Demo",
                dateUtc = "2024-01-25T16:00:00.000Z",
                rocketId = "falconheavy",
                launchpadId = "ksc_lc_39a",
                links =
                    LaunchLinks(
                        patch =
                            LaunchPatch(
                                small = "https://example.com/falcon_heavy_patch.png",
                                large = "https://example.com/falcon_heavy_patch_large.png",
                            ),
                        webcast = "https://youtube.com/watch?v=Lk4zQ2wP-Nc",
                    ),
                details = "Falcon Heavy demonstration",
                success = false,
                upcoming = false,
            ),
        )

    fun createSingleMockLaunch(): Launch =
        Launch(
            id = "launch_single",
            name = "Single Launch Test",
            dateUtc = "2024-02-01T12:00:00.000Z",
            rocketId = "falcon9",
            launchpadId = "ksc_lc_39a",
            links =
                LaunchLinks(
                    patch =
                        LaunchPatch(
                            small = "https://example.com/single_patch.png",
                            large = "https://example.com/single_patch_large.png",
                        ),
                    webcast = "https://youtube.com/watch?v=single",
                ),
            details = "Single launch test details",
            success = true,
            upcoming = true,
        )

    fun createMockSpaceXApiResponse(): String =
        """
        [
            {
                "id": "5eb87cd9ffd86e000604b32a",
                "name": "FalconSat",
                "date_utc": "2006-03-24T22:30:00.000Z",
                "success": false,
                "links": {
                    "patch": {
                        "small": "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
                        "large": "https://images2.imgbox.com/5b/02/QcxHUb5V_o.png"
                    },
                    "webcast": "https://www.youtube.com/watch?v=0a_00nJ_Y88"
                }
            },
            {
                "id": "5eb87cd9ffd86e000604b32b",
                "name": "DemoSat",
                "date_utc": "2007-03-21T01:10:00.000Z",
                "success": true,
                "links": {
                    "patch": {
                        "small": "https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png",
                        "large": "https://images2.imgbox.com/4f/e3/I0lkuJ2e_o.png"
                    },
                    "webcast": "https://www.youtube.com/watch?v=Lk4zQ2wP-Nc"
                }
            }
        ]
        """.trimIndent()

    fun createEmptyApiResponse(): String = "[]"

    fun createNullValuesApiResponse(): String =
        """
        [
            {
                "id": "5eb87cd9ffd86e000604b32c",
                "name": "Trailblazer",
                "date_utc": "2008-08-03T03:34:00.000Z",
                "success": null,
                "links": {
                    "patch": null,
                    "webcast": null
                }
            }
        ]
        """.trimIndent()

    fun createLargeApiResponse(count: Int = 10): String {
        val launches =
            (1..count).map { index ->
                """
                {
                    "id": "5eb87cd9ffd86e000604b3${index.toString().padStart(2, '0')}",
                    "name": "Launch $index",
                    "date_utc": "2024-0${index % 9 + 1}-${index.toString().padStart(
                    2,
                    '0',
                )}T${index.toString().padStart(2, '0')}:00:00.000Z",
                    "success": ${index % 2 == 0},
                    "links": {
                        "patch": {
                            "small": "https://images2.imgbox.com/patch${index}_small.png",
                            "large": "https://images2.imgbox.com/patch${index}_large.png"
                        },
                        "webcast": "https://www.youtube.com/watch?v=test$index"
                    }
                }
                """.trimIndent()
            }
        return "[${launches.joinToString(",")}]"
    }
}
