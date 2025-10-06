package com.linkjf.spacex.launch.home.di

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
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeModule {
    @Binds
    @Singleton
    abstract fun bindLaunchRepository(launchRepositoryImpl: LaunchRepositoryImpl): LaunchRepository

    @Binds
    @Singleton
    abstract fun bindGetUpcomingLaunchesUseCase(impl: GetUpcomingLaunchesUseCaseImpl): GetUpcomingLaunchesUseCase

    @Binds
    @Singleton
    abstract fun bindGetPastLaunchesUseCase(impl: GetPastLaunchesUseCaseImpl): GetPastLaunchesUseCase

    companion object {
        @Provides
        @Singleton
        fun provideLaunchLibraryApi(
            @Named("launchlibrary") retrofit: Retrofit,
        ): LaunchLibraryApi = retrofit.create(LaunchLibraryApi::class.java)
    }
}
