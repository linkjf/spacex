package com.linkjf.spacex_launch.home.data.remote

import com.linkjf.spacex_launch.home.data.remote.dto.LaunchDto
import retrofit2.http.GET

interface SpaceXApi {
    
    @GET("launches/upcoming")
    suspend fun getUpcomingLaunches(): List<LaunchDto>
}
