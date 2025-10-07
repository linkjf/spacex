package com.linkjf.spacex.launch.home.data.remote

import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLibraryLaunchDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchLibraryResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LaunchLibraryApi {
    @GET("launches/upcoming/")
    suspend fun getUpcomingLaunches(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("ordering") ordering: String = "net",
    ): LaunchLibraryResponseDto<LaunchLibraryLaunchDto>

    @GET("launches/previous/")
    suspend fun getPastLaunches(
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("ordering") ordering: String = "-net",
    ): LaunchLibraryResponseDto<LaunchLibraryLaunchDto>
}
