package com.linkjf.spacex.launch.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launches_remote_keys")
data class LaunchesRemoteKeysEntity(
    @PrimaryKey val launchId: String,
    val isUpcoming: Boolean,
    val prevOffset: Int?,
    val nextOffset: Int?,
)
