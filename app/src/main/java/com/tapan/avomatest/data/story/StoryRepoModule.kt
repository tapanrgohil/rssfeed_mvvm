package com.tapan.avomatest.data.story

import com.tapan.avomatest.data.feed.FeedLocalSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.contracts.ExperimentalContracts

@Module
@InstallIn(SingletonComponent::class)
abstract class StoryRepoModule {

    @Binds
    abstract fun storyLocalSource(storyLocalSource: StoryLocalSourceImpl): StoryLocalSource


    @ExperimentalContracts
    @Binds
    abstract fun storyRepo(storyRepositoryImpl: StoryRepositoryImpl): StoryRepository


}