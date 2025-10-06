package com.linkjf.spacex.launch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linkjf.spacex.launch.database.entity.LaunchpadEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for launchpad operations
 */
@Dao
interface LaunchpadDao {
    @Query("SELECT * FROM launchpads")
    fun getAllLaunchpads(): Flow<List<LaunchpadEntity>>

    @Query("SELECT * FROM launchpads WHERE id = :id")
    suspend fun getLaunchpadById(id: String): LaunchpadEntity?

    @Query("SELECT * FROM launchpads WHERE id IN (:ids)")
    suspend fun getLaunchpadsByIds(ids: List<String>): List<LaunchpadEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunchpad(launchpad: LaunchpadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunchpads(launchpads: List<LaunchpadEntity>)

    @Query("DELETE FROM launchpads")
    suspend fun deleteAllLaunchpads()

    @Query("SELECT * FROM launchpads WHERE lastUpdated < :timestamp")
    suspend fun getStaleLaunchpads(timestamp: Long): List<LaunchpadEntity>
}
