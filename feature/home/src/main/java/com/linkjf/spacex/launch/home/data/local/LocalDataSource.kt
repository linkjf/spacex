package com.linkjf.spacex.launch.home.data.local

import com.linkjf.spacex.launch.database.dao.LaunchDao
import com.linkjf.spacex.launch.database.dao.LaunchpadDao
import com.linkjf.spacex.launch.database.dao.RocketDao
import com.linkjf.spacex.launch.home.data.mapper.DatabaseMapper
import com.linkjf.spacex.launch.home.domain.model.Launch
import com.linkjf.spacex.launch.home.domain.model.Launchpad
import com.linkjf.spacex.launch.home.domain.model.Rocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Local data source for offline storage operations
 */
@Singleton
class LocalDataSource
    @Inject
    constructor(
        private val launchDao: LaunchDao,
        private val rocketDao: RocketDao,
        private val launchpadDao: LaunchpadDao,
    ) {
        // Launch operations
        fun getUpcomingLaunches(): Flow<List<Launch>> =
            launchDao
                .getLaunchesByType(isUpcoming = true)
                .map { entities -> entities.map { DatabaseMapper.run { it.toDomain() } } }

        fun getPastLaunches(): Flow<List<Launch>> =
            launchDao
                .getLaunchesByType(isUpcoming = false)
                .map { entities -> entities.map { DatabaseMapper.run { it.toDomain() } } }

        fun getUpcomingLaunchesPaginated(
            limit: Int,
            offset: Int,
        ): Flow<List<Launch>> =
            launchDao
                .getLaunchesByTypePaginated(isUpcoming = true, limit, offset)
                .map { entities -> entities.map { DatabaseMapper.run { it.toDomain() } } }

        fun getPastLaunchesPaginated(
            limit: Int,
            offset: Int,
        ): Flow<List<Launch>> =
            launchDao
                .getLaunchesByTypePaginated(isUpcoming = false, limit, offset)
                .map { entities -> entities.map { DatabaseMapper.run { it.toDomain() } } }

        suspend fun getUpcomingLaunchCount(): Int = launchDao.getLaunchCountByType(isUpcoming = true)

        suspend fun getPastLaunchCount(): Int = launchDao.getLaunchCountByType(isUpcoming = false)

        suspend fun insertLaunches(launches: List<Launch>) {
            val entities = launches.map { DatabaseMapper.run { it.toEntity() } }
            launchDao.insertLaunches(entities)
        }

        suspend fun insertLaunch(launch: Launch) {
            launchDao.insertLaunch(DatabaseMapper.run { launch.toEntity() })
        }

        suspend fun deleteUpcomingLaunches() {
            launchDao.deleteLaunchesByType(isUpcoming = true)
        }

        suspend fun deletePastLaunches() {
            launchDao.deleteLaunchesByType(isUpcoming = false)
        }

        // Rocket operations
        fun getAllRockets(): Flow<List<Rocket>> =
            rocketDao
                .getAllRockets()
                .map { entities -> entities.map { DatabaseMapper.run { it.toDomain() } } }

        suspend fun getRocketById(id: String): Rocket? = rocketDao.getRocketById(id)?.let { DatabaseMapper.run { it.toDomain() } }

        suspend fun getRocketsByIds(ids: List<String>): List<Rocket> =
            rocketDao.getRocketsByIds(ids).map { DatabaseMapper.run { it.toDomain() } }

        suspend fun insertRockets(rockets: List<Rocket>) {
            val entities = rockets.map { DatabaseMapper.run { it.toEntity() } }
            rocketDao.insertRockets(entities)
        }

        suspend fun insertRocket(rocket: Rocket) {
            rocketDao.insertRocket(DatabaseMapper.run { rocket.toEntity() })
        }

        suspend fun deleteAllRockets() {
            rocketDao.deleteAllRockets()
        }

        // Launchpad operations
        fun getAllLaunchpads(): Flow<List<Launchpad>> =
            launchpadDao
                .getAllLaunchpads()
                .map { entities -> entities.map { DatabaseMapper.run { it.toDomain() } } }

        suspend fun getLaunchpadById(id: String): Launchpad? =
            launchpadDao.getLaunchpadById(id)?.let { DatabaseMapper.run { it.toDomain() } }

        suspend fun getLaunchpadsByIds(ids: List<String>): List<Launchpad> =
            launchpadDao.getLaunchpadsByIds(ids).map {
                DatabaseMapper.run { it.toDomain() }
            }

        suspend fun insertLaunchpads(launchpads: List<Launchpad>) {
            val entities = launchpads.map { DatabaseMapper.run { it.toEntity() } }
            launchpadDao.insertLaunchpads(entities)
        }

        suspend fun insertLaunchpad(launchpad: Launchpad) {
            launchpadDao.insertLaunchpad(DatabaseMapper.run { launchpad.toEntity() })
        }

        suspend fun deleteAllLaunchpads() {
            launchpadDao.deleteAllLaunchpads()
        }

        // Cache management
        suspend fun clearAllData() {
            launchDao.deleteAllLaunches()
            rocketDao.deleteAllRockets()
            launchpadDao.deleteAllLaunchpads()
        }

        suspend fun getStaleDataTimestamp(): Long {
            val currentTime = System.currentTimeMillis()
            val cacheExpiryTime = 24 * 60 * 60 * 1000L // 24 hours
            return currentTime - cacheExpiryTime
        }
    }
