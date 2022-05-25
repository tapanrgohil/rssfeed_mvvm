package com.tapan.avomatest.data.di

import android.app.Application
import androidx.room.Room
import com.tapan.avomatest.data.db.FeedDatabase
import com.tapan.avomatest.data.feed.FeedDao
import com.tapan.avomatest.data.story.StoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "feed-db"

    @Provides
    @Singleton
    fun provideDatabase(application: Application): FeedDatabase {
        return Room.databaseBuilder(
            application, FeedDatabase::class.java, DB_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideStory(database: FeedDatabase): StoryDao = database.storyDao()

    @Provides
    fun provideFeedDao(database: FeedDatabase): FeedDao = database.feedDao()


}