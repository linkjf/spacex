package com.linkjf.spacex.launch.home.di

import com.linkjf.spacex.launch.home.data.remote.SpaceXApi
import com.linkjf.spacex.launch.home.data.repository.LaunchRepositoryImpl
import com.linkjf.spacex.launch.home.domain.repository.LaunchRepository
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
    abstract fun bindLaunchRepository(
        launchRepositoryImpl: LaunchRepositoryImpl
    ): LaunchRepository
    
    companion object {
        
        @Provides
        @Singleton
        fun provideSpaceXApi(
            @Named("spacex") retrofit: Retrofit
        ): SpaceXApi {
            return retrofit.create(SpaceXApi::class.java)
        }
    }
}
