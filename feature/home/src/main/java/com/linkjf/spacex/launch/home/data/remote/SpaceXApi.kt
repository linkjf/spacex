package com.linkjf.spacex.launch.home.data.remote

import com.linkjf.spacex.launch.home.data.remote.dto.LaunchDto
import retrofit2.http.GET

interface SpaceXApi {
    
    @GET("launches/upcoming")
    suspend fun getUpcomingLaunches(): List<LaunchDto>
}
