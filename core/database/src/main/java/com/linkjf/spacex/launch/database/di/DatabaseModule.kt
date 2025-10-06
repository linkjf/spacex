package com.linkjf.spacex.launch.database.di

import android.content.Context
import com.linkjf.spacex.launch.database.SpaceXDatabase
import com.linkjf.spacex.launch.database.dao.LaunchDao
import com.linkjf.spacex.launch.database.dao.LaunchRemoteKeysDao
import com.linkjf.spacex.launch.database.dao.LaunchpadDao
import com.linkjf.spacex.launch.database.dao.RocketDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSpaceXDatabase(
        @ApplicationContext context: Context,
    ): SpaceXDatabase = SpaceXDatabase.getDatabase(context)

    @Provides
    fun provideLaunchDao(database: SpaceXDatabase): LaunchDao = database.launchDao()

    @Provides
    fun provideRocketDao(database: SpaceXDatabase): RocketDao = database.rocketDao()

    @Provides
    fun provideLaunchpadDao(database: SpaceXDatabase): LaunchpadDao = database.launchpadDao()

    @Provides
    fun provideRemoteKeysDao(database: SpaceXDatabase): LaunchRemoteKeysDao = database.launchRemoteKeyDao()
}
