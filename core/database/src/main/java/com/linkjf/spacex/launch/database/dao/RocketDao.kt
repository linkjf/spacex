package com.linkjf.spacex.launch.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linkjf.spacex.launch.database.entity.RocketEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for rocket operations
 */
@Dao
interface RocketDao {
    @Query("SELECT * FROM rockets")
    fun getAllRockets(): Flow<List<RocketEntity>>

    @Query("SELECT * FROM rockets WHERE id = :id")
    suspend fun getRocketById(id: String): RocketEntity?

    @Query("SELECT * FROM rockets WHERE id IN (:ids)")
    suspend fun getRocketsByIds(ids: List<String>): List<RocketEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRocket(rocket: RocketEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRockets(rockets: List<RocketEntity>)

    @Query("DELETE FROM rockets")
    suspend fun deleteAllRockets()

    @Query("SELECT * FROM rockets WHERE lastUpdated < :timestamp")
    suspend fun getStaleRockets(timestamp: Long): List<RocketEntity>
}
