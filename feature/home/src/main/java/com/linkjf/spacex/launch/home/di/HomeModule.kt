package com.linkjf.spacex.launch.home.di

import com.linkjf.spacex.launch.home.data.mapper.LaunchLibraryLaunchMapper
import com.linkjf.spacex.launch.home.data.mapper.LaunchToLaunchListItemMapper
import com.linkjf.spacex.launch.home.data.remote.LaunchLibraryApi
import com.linkjf.spacex.launch.home.data.repository.LaunchRepositoryImpl
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCaseImpl
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetUpcomingLaunchesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {
    @Binds
    abstract fun bindGetUpcomingLaunchesUseCase(getUpcomingLaunchesUseCaseImpl: GetUpcomingLaunchesUseCaseImpl): GetUpcomingLaunchesUseCase

    @Binds
    abstract fun bindGetPastLaunchesUseCase(getPastLaunchesUseCaseImpl: GetPastLaunchesUseCaseImpl): GetPastLaunchesUseCase

    @Binds
    abstract fun bindLaunchRepository(launchRepositoryImpl: LaunchRepositoryImpl): LaunchRepository

    companion object {
        @Provides
        @Singleton
        fun provideLaunchToLaunchListItemMapper(): LaunchToLaunchListItemMapper = LaunchToLaunchListItemMapper()

        @Provides
        @Singleton
        fun provideLaunchLibraryLaunchMapper(): LaunchLibraryLaunchMapper = LaunchLibraryLaunchMapper

        @Provides
        @Singleton
        fun provideLaunchLibraryApi(
            @Named("launchlibrary") retrofit: retrofit2.Retrofit,
        ): LaunchLibraryApi = retrofit.create(LaunchLibraryApi::class.java)
    }
}
