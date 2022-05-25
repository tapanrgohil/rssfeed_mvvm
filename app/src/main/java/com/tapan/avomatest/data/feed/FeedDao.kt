package com.tapan.avomatest.data.feed

import androidx.room.Dao
import androidx.room.Query
import com.tapan.avomatest.data.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FeedDao : BaseDao<FeedEntity>() {

    @Query("SELECT * FROM FEED")
    abstract fun getAllFeeds(): Flow<List<FeedEntity>>

    @Query("DELETE FROM FEED WHERE LINK =:link")
    abstract fun deleteFeedByLink(link: String):Int
}