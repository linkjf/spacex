package com.linkjf.spacex.launch.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.linkjf.spacex.launch.database.entity.LaunchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LaunchDao {
    @Query(
        """
        SELECT * FROM launches 
        WHERE upcoming = :isUpcoming 
        ORDER BY 
            CASE WHEN :isUpcoming = 1 THEN dateUtc END ASC,
            CASE WHEN :isUpcoming = 0 THEN dateUtc END DESC
        """,
    )
    fun pagingSource(isUpcoming: Boolean): PagingSource<Int, LaunchEntity>

    @Query(
        """
        SELECT * FROM launches 
        WHERE upcoming = :isUpcoming 
        ORDER BY 
            CASE WHEN :isUpcoming = 1 THEN dateUtc END ASC,
            CASE WHEN :isUpcoming = 0 THEN dateUtc END DESC
        LIMIT :limit OFFSET :offset
        """,
    )
    fun getLaunchesByTypePaginated(
        isUpcoming: Boolean,
        limit: Int,
        offset: Int,
    ): Flow<List<LaunchEntity>>

    @Query("SELECT COUNT(*) FROM launches WHERE upcoming = :isUpcoming")
    suspend fun getLaunchCountByType(isUpcoming: Boolean): Int

    @Query("SELECT * FROM launches WHERE id = :id")
    suspend fun getLaunchById(id: String): LaunchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunch(launch: LaunchEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaunches(launches: List<LaunchEntity>)

    @Query("DELETE FROM launches WHERE upcoming = :isUpcoming")
    suspend fun deleteByType(isUpcoming: Boolean)

    @Query("DELETE FROM launches")
    suspend fun deleteAllLaunches()

    @Query("SELECT * FROM launches WHERE lastUpdated < :timestamp")
    suspend fun getStaleLaunches(timestamp: Long): List<LaunchEntity>
}
