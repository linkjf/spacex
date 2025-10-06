package com.linkjf.spacex.launch.home.di

import com.linkjf.spacex.launch.database.dao.LaunchDao
import com.linkjf.spacex.launch.database.dao.LaunchpadDao
import com.linkjf.spacex.launch.database.dao.RocketDao
import com.linkjf.spacex.launch.home.data.local.LocalDataSource
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.home.data.repository.LaunchRepositoryImpl
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCaseImpl
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCaseImpl
import javax.inject.Named
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

/**
 * Hilt module for home feature dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {
    @Binds
    abstract fun bindGetUpcomingLaunchesUseCase(
        getUpcomingLaunchesUseCaseImpl: GetUpcomingLaunchesUseCaseImpl,
    ): GetUpcomingLaunchesUseCase

    @Binds
    abstract fun bindGetPastLaunchesUseCase(
        getPastLaunchesUseCaseImpl: GetPastLaunchesUseCaseImpl,
    ): GetPastLaunchesUseCase

    @Binds
    abstract fun bindLaunchRepository(
        launchRepositoryImpl: LaunchRepositoryImpl,
    ): LaunchRepository

    companion object {
        @Provides
        @Singleton
        fun provideLocalDataSource(
            launchDao: LaunchDao,
            rocketDao: RocketDao,
            launchpadDao: LaunchpadDao,
        ): LocalDataSource = LocalDataSource(launchDao, rocketDao, launchpadDao)

        @Provides
        @Singleton
        fun provideLaunchLibraryApi(
            @Named("launchlibrary") retrofit: retrofit2.Retrofit,
        ): LaunchLibraryApi = retrofit.create(LaunchLibraryApi::class.java)
    }
}
