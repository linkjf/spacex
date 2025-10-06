package com.linkjf.spacex.launch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linkjf.spacex.launch.database.entity.LaunchesRemoteKeysEntity

@Dao
interface LaunchRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(keys: List<LaunchesRemoteKeysEntity>)

    @Query("SELECT * FROM launches_remote_keys WHERE launchId = :id AND isUpcoming = :isUpcoming")
    suspend fun remoteKeysByLaunch(
        id: String,
        isUpcoming: Boolean,
    ): LaunchesRemoteKeysEntity?

    @Query("DELETE FROM launches_remote_keys WHERE isUpcoming = :isUpcoming")
    suspend fun clearByType(isUpcoming: Boolean)

    @Query("DELETE FROM launches_remote_keys")
    suspend fun clear()
}
