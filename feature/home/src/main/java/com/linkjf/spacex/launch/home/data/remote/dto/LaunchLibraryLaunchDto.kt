package com.linkjf.spacex.launch.home.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchLibraryLaunchDto(
    @SerialName("id")
    val id: String,
    @SerialName("url")
    val url: String,
    @SerialName("name")
    val name: String,
    @SerialName("response_mode")
    val responseMode: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("launch_designator")
    val launchDesignator: String? = null,
    @SerialName("status")
    val status: LaunchStatusDto,
    @SerialName("last_updated")
    val lastUpdated: String,
    @SerialName("net")
    val net: String,
    @SerialName("net_precision")
    val netPrecision: NetPrecisionDto,
    @SerialName("window_end")
    val windowEnd: String? = null,
    @SerialName("window_start")
    val windowStart: String? = null,
    @SerialName("image")
    val image: LaunchImageDto? = null,
    @SerialName("infographic")
    val infographic: String? = null,
    @SerialName("probability")
    val probability: Int? = null,
    @SerialName("weather_concerns")
    val weatherConcerns: String? = null,
    @SerialName("failreason")
    val failReason: String? = null,
    @SerialName("hashtag")
    val hashtag: String? = null,
    @SerialName("launch_service_provider")
    val launchServiceProvider: LaunchServiceProviderDto,
    @SerialName("rocket")
    val rocket: LaunchRocketDto,
    @SerialName("mission")
    val mission: LaunchMissionDto? = null,
    @SerialName("pad")
    val pad: LaunchPadDto,
    @SerialName("webcast_live")
    val webcastLive: Boolean? = null,
    @SerialName("program")
    val program: List<LaunchProgramDto>? = null,
    @SerialName("orbital_launch_attempt_count")
    val orbitalLaunchAttemptCount: Int? = null,
    @SerialName("location_launch_attempt_count")
    val locationLaunchAttemptCount: Int? = null,
    @SerialName("pad_launch_attempt_count")
    val padLaunchAttemptCount: Int? = null,
    @SerialName("agency_launch_attempt_count")
    val agencyLaunchAttemptCount: Int? = null,
    @SerialName("orbital_launch_attempt_count_year")
    val orbitalLaunchAttemptCountYear: Int? = null,
    @SerialName("location_launch_attempt_count_year")
    val locationLaunchAttemptCountYear: Int? = null,
    @SerialName("pad_launch_attempt_count_year")
    val padLaunchAttemptCountYear: Int? = null,
    @SerialName("agency_launch_attempt_count_year")
    val agencyLaunchAttemptCountYear: Int? = null,
)

@Serializable
data class LaunchStatusDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("abbrev")
    val abbrev: String? = null,
    @SerialName("description")
    val description: String? = null,
)

@Serializable
data class NetPrecisionDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("abbrev")
    val abbrev: String? = null,
    @SerialName("description")
    val description: String? = null,
)

@Serializable
data class LaunchImageDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String? = null,
    @SerialName("credit")
    val credit: String?,
    @SerialName("license")
    val license: ImageLicenseDto? = null,
    @SerialName("single_use")
    val singleUse: Boolean? = null,
    @SerialName("variants")
    val variants: List<String>? = null,
)

@Serializable
data class ImageLicenseDto(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("priority")
    val priority: Int? = null,
    @SerialName("link")
    val link: String? = null,
)

@Serializable
data class LaunchServiceProviderDto(
    @SerialName("response_mode")
    val responseMode: String,
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String,
    @SerialName("name")
    val name: String,
    @SerialName("abbrev")
    val abbrev: String? = null,
    @SerialName("type")
    val type: AgencyTypeDto,
)

@Serializable
data class AgencyTypeDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
data class LaunchRocketDto(
    @SerialName("id")
    val id: Int,
    @SerialName("configuration")
    val configuration: RocketConfigurationDto,
)

@Serializable
data class RocketConfigurationDto(
    @SerialName("response_mode")
    val responseMode: String,
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String,
    @SerialName("name")
    val name: String,
    @SerialName("families")
    val families: List<RocketFamilyDto>,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("variant")
    val variant: String,
)

@Serializable
data class RocketFamilyDto(
    @SerialName("response_mode")
    val responseMode: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
data class LaunchMissionDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("type")
    val type: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("orbit")
    val orbit: MissionOrbitDto? = null,
    @SerialName("agencies")
    val agencies: List<LaunchAgencyDto>? = null,
    @SerialName("info_urls")
    val infoUrls: List<String>? = null,
    @SerialName("vid_urls")
    val vidUrls: List<String>? = null,
)

@Serializable
data class MissionOrbitDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("abbrev")
    val abbrev: String? = null,
    @SerialName("celestial_body")
    val celestialBody: CelestialBodyDto,
)

@Serializable
data class CelestialBodyDto(
    @SerialName("response_mode")
    val responseMode: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
data class LaunchAgencyDto(
    @SerialName("response_mode")
    val responseMode: String,
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String,
    @SerialName("name")
    val name: String,
    @SerialName("abbrev")
    val abbrev: String? = null,
    @SerialName("type")
    val type: AgencyTypeDto,
    @SerialName("featured")
    val featured: Boolean? = null,
    @SerialName("country")
    val country: List<CountryDto>? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("administrator")
    val administrator: String? = null,
    @SerialName("founding_year")
    val foundingYear: Int? = null,
    @SerialName("launchers")
    val launchers: String? = null,
    @SerialName("spacecraft")
    val spacecraft: String? = null,
    @SerialName("parent")
    val parent: String? = null,
    @SerialName("image")
    val image: LaunchImageDto? = null,
    @SerialName("logo")
    val logo: LaunchImageDto? = null,
    @SerialName("social_logo")
    val socialLogo: LaunchImageDto? = null,
    @SerialName("total_launch_count")
    val totalLaunchCount: Int? = null,
    @SerialName("consecutive_successful_launches")
    val consecutiveSuccessfulLaunches: Int? = null,
    @SerialName("successful_launches")
    val successfulLaunches: Int? = null,
    @SerialName("failed_launches")
    val failedLaunches: Int? = null,
    @SerialName("pending_launches")
    val pendingLaunches: Int? = null,
    @SerialName("consecutive_successful_landings")
    val consecutiveSuccessfulLandings: Int? = null,
    @SerialName("successful_landings")
    val successfulLandings: Int? = null,
    @SerialName("failed_landings")
    val failedLandings: Int? = null,
    @SerialName("attempted_landings")
    val attemptedLandings: Int? = null,
    @SerialName("successful_landings_spacecraft")
    val successfulLandingsSpacecraft: Int? = null,
    @SerialName("failed_landings_spacecraft")
    val failedLandingsSpacecraft: Int? = null,
    @SerialName("attempted_landings_spacecraft")
    val attemptedLandingsSpacecraft: Int? = null,
    @SerialName("successful_landings_payload")
    val successfulLandingsPayload: Int? = null,
    @SerialName("failed_landings_payload")
    val failedLandingsPayload: Int? = null,
    @SerialName("attempted_landings_payload")
    val attemptedLandingsPayload: Int? = null,
    @SerialName("info_url")
    val infoUrl: String? = null,
    @SerialName("wiki_url")
    val wikiUrl: String? = null,
    @SerialName("social_media_links")
    val socialMediaLinks: List<String>? = null,
)

@Serializable
data class CountryDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("alpha_2_code")
    val alpha2Code: String,
    @SerialName("alpha_3_code")
    val alpha3Code: String,
    @SerialName("nationality_name")
    val nationalityName: String,
    @SerialName("nationality_name_composed")
    val nationalityNameComposed: String,
)

@Serializable
data class LaunchPadDto(
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String,
    @SerialName("active")
    val active: Boolean? = null,
    @SerialName("agencies")
    val agencies: List<LaunchAgencyDto>? = null,
    @SerialName("name")
    val name: String,
    @SerialName("image")
    val image: LaunchImageDto? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("info_url")
    val infoUrl: String? = null,
    @SerialName("wiki_url")
    val wikiUrl: String? = null,
    @SerialName("map_url")
    val mapUrl: String? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("country")
    val country: CountryDto,
    @SerialName("map_image")
    val mapImage: String? = null,
    @SerialName("total_launch_count")
    val totalLaunchCount: Int,
    @SerialName("orbital_launch_attempt_count")
    val orbitalLaunchAttemptCount: Int,
    @SerialName("fastest_turnaround")
    val fastestTurnaround: String? = null,
    @SerialName("location")
    val location: LaunchLocationDto,
)

@Serializable
data class LaunchLocationDto(
    @SerialName("response_mode")
    val responseMode: String,
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String,
    @SerialName("name")
    val name: String,
    @SerialName("celestial_body")
    val celestialBody: CelestialBodyDto,
    @SerialName("active")
    val active: Boolean? = null,
    @SerialName("country")
    val country: CountryDto,
    @SerialName("description")
    val description: String? = null,
    @SerialName("image")
    val image: LaunchImageDto? = null,
    @SerialName("map_image")
    val mapImage: String? = null,
    @SerialName("longitude")
    val longitude: Double? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("timezone_name")
    val timezoneName: String? = null,
    @SerialName("total_launch_count")
    val totalLaunchCount: Int,
    @SerialName("total_landing_count")
    val totalLandingCount: Int? = null,
)

@Serializable
data class LaunchProgramDto(
    @SerialName("response_mode")
    val responseMode: String,
    @SerialName("id")
    val id: Int,
    @SerialName("url")
    val url: String,
    @SerialName("name")
    val name: String,
    @SerialName("image")
    val image: LaunchImageDto? = null,
    @SerialName("info_url")
    val infoUrl: String? = null,
    @SerialName("wiki_url")
    val wikiUrl: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("agencies")
    val agencies: List<LaunchAgencyDto>? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("mission_patches")
    val missionPatches: List<MissionPatchDto>? = null,
    @SerialName("type")
    val type: ProgramTypeDto? = null,
)

@Serializable
data class MissionPatchDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("priority")
    val priority: Int,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("agency")
    val agency: LaunchAgencyDto,
    @SerialName("response_mode")
    val responseMode: String,
)

@Serializable
data class ProgramTypeDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
)

@Serializable
data class LaunchLibraryResponseDto<T>(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String? = null,
    @SerialName("previous")
    val previous: String? = null,
    @SerialName("results")
    val results: List<T>,
)
