package com.linkjf.spacex.launch.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "launches",
    indices = [Index(value = ["upcoming", "dateUtc"], name = "index_launches_upcoming_date")],
)
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
    val payloads: String? = null,
    val capsules: String? = null,
    val ships: String? = null,
    val crew: String? = null,
    val cores: String? = null,
    val fairings: String? = null,
    val autoUpdate: Boolean? = null,
    val dateLocal: String? = null,
    val datePrecision: String? = null,
    val dateUnix: Long? = null,
    val links: String? = null,
    val lastUpdated: Long = System.currentTimeMillis(),
)
