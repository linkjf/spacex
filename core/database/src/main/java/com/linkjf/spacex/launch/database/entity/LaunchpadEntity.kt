package com.linkjf.spacex.launch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Database entity for storing launchpad data locally
 */
@Entity(tableName = "launchpads")
@Serializable
data class LaunchpadEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val fullName: String,
    val status: String,
    val locality: String,
    val region: String,
    val timezone: String,
    val latitude: Double,
    val longitude: Double,
    val launchAttempts: Int,
    val launchSuccesses: Int,
    val rockets: String? = null, // JSON serialized list
    val launches: String? = null, // JSON serialized list
    val details: String,
    val lastUpdated: Long = System.currentTimeMillis(),
)
