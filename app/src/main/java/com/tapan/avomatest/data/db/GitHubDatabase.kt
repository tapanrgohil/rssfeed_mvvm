package com.tapan.avomatest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tapan.avomatest.data.feed.FeedDao
import com.tapan.avomatest.data.feed.FeedEntity
import com.tapan.avomatest.data.story.StoryDao
import com.tapan.avomatest.data.story.StoryEntity

@Database(
    entities = [StoryEntity::class,FeedEntity::class],
    version = 1
)
@TypeConverters(DateTimeConverter::class)
abstract class FeedDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun storyDao(): StoryDao

}