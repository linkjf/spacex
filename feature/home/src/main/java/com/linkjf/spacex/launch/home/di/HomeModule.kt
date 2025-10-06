package com.linkjf.spacex.launch.home.di

import com.linkjf.spacex.launch.home.data.remote.SpaceXApi
import com.linkjf.spacex.launch.home.data.repository.LaunchRepositoryImpl
import com.linkjf.spacex.launch.home.data.repository.LaunchpadRepositoryImpl
import com.linkjf.spacex.launch.home.data.repository.RocketRepositoryImpl
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
import com.linkjf.spacex.launch.home.domain.repository.LaunchpadRepository
import com.linkjf.spacex.launch.home.domain.repository.RocketRepository
import com.linkjf.spacex.launch.home.domain.usecase.GetLaunchpadUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetLaunchpadUseCaseImpl
import com.linkjf.spacex.launch.home.domain.usecase.GetLaunchpadsUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetLaunchpadsUseCaseImpl
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetPastLaunchesUseCaseImpl
import com.linkjf.spacex.launch.home.domain.usecase.GetRocketUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetRocketUseCaseImpl
import com.linkjf.spacex.launch.home.domain.usecase.GetRocketsUseCase
import com.linkjf.spacex.launch.home.domain.usecase.GetRocketsUseCaseImpl
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
    abstract fun bindRocketRepository(rocketRepositoryImpl: RocketRepositoryImpl): RocketRepository

    @Binds
    @Singleton
    abstract fun bindLaunchpadRepository(launchpadRepositoryImpl: LaunchpadRepositoryImpl): LaunchpadRepository

    @Binds
    @Singleton
    abstract fun bindGetUpcomingLaunchesUseCase(impl: GetUpcomingLaunchesUseCaseImpl): GetUpcomingLaunchesUseCase

    @Binds
    @Singleton
    abstract fun bindGetPastLaunchesUseCase(impl: GetPastLaunchesUseCaseImpl): GetPastLaunchesUseCase

    @Binds
    @Singleton
    abstract fun bindGetRocketsUseCase(impl: GetRocketsUseCaseImpl): GetRocketsUseCase

    @Binds
    @Singleton
    abstract fun bindGetRocketUseCase(impl: GetRocketUseCaseImpl): GetRocketUseCase

    @Binds
    @Singleton
    abstract fun bindGetLaunchpadsUseCase(impl: GetLaunchpadsUseCaseImpl): GetLaunchpadsUseCase

    @Binds
    @Singleton
    abstract fun bindGetLaunchpadUseCase(impl: GetLaunchpadUseCaseImpl): GetLaunchpadUseCase

    companion object {
        @Provides
        @Singleton
        fun provideSpaceXApi(
            @Named("spacex") retrofit: Retrofit,
        ): SpaceXApi = retrofit.create(SpaceXApi::class.java)
    }
}
