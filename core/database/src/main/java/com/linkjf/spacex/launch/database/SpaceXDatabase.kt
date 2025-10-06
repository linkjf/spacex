package com.linkjf.spacex.launch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.linkjf.spacex.launch.database.converter.Converters
import com.linkjf.spacex.launch.database.dao.LaunchDao
import com.linkjf.spacex.launch.database.dao.LaunchRemoteKeysDao
import com.linkjf.spacex.launch.database.dao.LaunchpadDao
import com.linkjf.spacex.launch.database.dao.RocketDao
import com.linkjf.spacex.launch.database.entity.LaunchEntity
import com.linkjf.spacex.launch.database.entity.LaunchesRemoteKeysEntity
import com.linkjf.spacex.launch.database.entity.LaunchpadEntity
import com.linkjf.spacex.launch.database.entity.RocketEntity

/**
 * Room database for SpaceX Launch app
 */
@Database(
    entities = [
        LaunchEntity::class,
        RocketEntity::class,
        LaunchpadEntity::class,
        LaunchesRemoteKeysEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class SpaceXDatabase : RoomDatabase() {
    abstract fun launchDao(): LaunchDao

    abstract fun launchRemoteKeyDao(): LaunchRemoteKeysDao

    abstract fun rocketDao(): RocketDao

    abstract fun launchpadDao(): LaunchpadDao

    companion object {
        const val DATABASE_NAME = "spacex_database"

        @Volatile
        private var instance: SpaceXDatabase? = null

        fun getDatabase(context: Context): SpaceXDatabase =
            instance ?: synchronized(this) {
                val databaseInstance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            SpaceXDatabase::class.java,
                            DATABASE_NAME,
                        ).fallbackToDestructiveMigration(false)
                        .build()
                instance = databaseInstance
                databaseInstance
            }
    }
}
