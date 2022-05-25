package com.tapan.avomatest.data.feed.di

import com.tapan.avomatest.data.feed.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module(includes = [FeedRepoModule::class])
@InstallIn(SingletonComponent::class)
class FeedModule {


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): FeedNetworkService {
        return retrofit.create(FeedNetworkService::class.java)
    }


}