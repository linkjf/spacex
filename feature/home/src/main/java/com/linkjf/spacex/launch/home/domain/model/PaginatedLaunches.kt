package com.linkjf.spacex.launch.home.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a paginated response of launches
 */
@Serializable
data class PaginatedLaunches(
    val launches: List<Launch>,
    val totalCount: Int,
    val hasMore: Boolean,
)
