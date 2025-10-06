package com.linkjf.spacex.launch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Database entity for storing rocket data locally
 */
@Entity(tableName = "rockets")
@Serializable
data class RocketEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val active: Boolean,
    val stages: Int,
    val boosters: Int,
    val costPerLaunch: Long,
    val successRatePct: Double,
    val firstFlight: String,
    val country: String,
    val company: String,
    val height: String? = null, // JSON serialized Dimension
    val diameter: String? = null, // JSON serialized Dimension
    val mass: String? = null, // JSON serialized Mass
    val payloadWeights: String? = null, // JSON serialized list
    val firstStage: String? = null, // JSON serialized Stage
    val secondStage: String? = null, // JSON serialized Stage
    val engines: String? = null, // JSON serialized Engine
    val landingLegs: String? = null, // JSON serialized LandingLegs
    val flickrImages: String? = null, // JSON serialized list
    val wikipedia: String,
    val description: String,
    val lastUpdated: Long = System.currentTimeMillis(),
)
