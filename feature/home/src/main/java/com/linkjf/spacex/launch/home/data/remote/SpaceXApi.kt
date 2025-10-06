package com.linkjf.spacex.launch.home.data.remote

import com.linkjf.spacex.launch.home.data.remote.dto.LaunchDto
import com.linkjf.spacex.launch.home.data.remote.dto.LaunchpadDto
import com.linkjf.spacex.launch.home.data.remote.dto.RocketDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SpaceXApi {
    @GET("launches/upcoming")
    suspend fun getUpcomingLaunches(): List<LaunchDto>

    @GET("launches/past")
    suspend fun getPastLaunches(): List<LaunchDto>

    @GET("rockets")
    suspend fun getRockets(): List<RocketDto>

    @GET("rockets/{id}")
    suspend fun getRocket(
        @Path("id") id: String,
    ): RocketDto

    @GET("launchpads")
    suspend fun getLaunchpads(): List<LaunchpadDto>

    @GET("launchpads/{id}")
    suspend fun getLaunchpad(
        @Path("id") id: String,
    ): LaunchpadDto
}
