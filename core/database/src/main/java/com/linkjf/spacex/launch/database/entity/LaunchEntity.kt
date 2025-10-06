package com.linkjf.spacex.launch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Database entity for storing launch data locally
 */
@Entity(tableName = "launches")
@Serializable
data class LaunchEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val dateUtc: String,
    val rocketId: String,
    val launchpadId: String,
    val details: String? = null,
    val success: Boolean? = null,
    val upcoming: Boolean,
    val flightNumber: Int? = null,
    val staticFireDateUtc: String? = null,
    val tbd: Boolean? = null,
    val net: Boolean? = null,
    val window: Int? = null,
    val payloads: String? = null, // JSON serialized list
    val capsules: String? = null, // JSON serialized list
    val ships: String? = null, // JSON serialized list
    val crew: String? = null, // JSON serialized list
    val cores: String? = null, // JSON serialized list
    val fairings: String? = null, // JSON serialized object
    val autoUpdate: Boolean? = null,
    val dateLocal: String? = null,
    val datePrecision: String? = null,
    val dateUnix: Long? = null,
    val links: String? = null, // JSON serialized LaunchLinks
    val lastUpdated: Long = System.currentTimeMillis(),
)
