package com.linkjf.spacex.launch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

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
    val height: String? = null,
    val diameter: String? = null,
    val mass: String? = null,
    val payloadWeights: String? = null,
    val firstStage: String? = null,
    val secondStage: String? = null,
    val engines: String? = null,
    val landingLegs: String? = null,
    val flickrImages: String? = null,
    val wikipedia: String,
    val description: String,
    val lastUpdated: Long = System.currentTimeMillis(),
)
