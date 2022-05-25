package com.tapan.avomatest.data.feed.di

import com.tapan.avomatest.data.feed.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.contracts.ExperimentalContracts


@Module
@InstallIn(SingletonComponent::class)
abstract class FeedRepoModule {
    @Binds
    abstract fun feedLocalSource(feedLocalSource: FeedLocalSourceImpl): FeedLocalSource

    @Binds
    abstract fun feedRemoteSource(feedRemoteSource: FeedRemoteSourceImpl): FeedRemoteSource


    @ExperimentalContracts
    @Binds
    abstract fun feedRepo(feedRepositoryImpl: FeedRepositoryImpl): FeedRepository

}