package com.tapan.avomatest.data.story

import androidx.room.Dao
import androidx.room.Query
import com.tapan.avomatest.data.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class StoryDao : BaseDao<StoryEntity>() {

    @Query("DELETE FROM STORY WHERE LINK = :link")
    abstract fun deleteFeeByLink(link: String): Int

    @Query("SELECT * FROM STORY WHERE FEED_URL =:link")
    abstract fun getAllStoriesByFeed(link: String): List<StoryEntity>

    @Query("SELECT * FROM STORY")
    abstract fun getAllStories(): Flow<List<StoryEntity>>

}