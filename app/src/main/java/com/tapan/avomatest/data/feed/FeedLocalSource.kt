package com.tapan.avomatest.data.feed

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FeedLocalSource {

    fun getAllFeeds(): Flow<List<FeedEntity>>
    fun addFeed(link: String): Long
    fun deleteFeed(link: String): Int
}

class FeedLocalSourceImpl @Inject constructor(private val feedDao: FeedDao) : FeedLocalSource {
    override fun getAllFeeds(): Flow<List<FeedEntity>> {
        return feedDao.getAllFeeds()
    }

    override fun addFeed(link: String): Long {
        return feedDao.insert(FeedEntity(link))
    }

    override fun deleteFeed(link: String): Int {
        return feedDao.deleteFeedByLink(link)
    }

}