package com.tapan.avomatest

import com.tapan.avomatest.data.base.Resource
import com.tapan.avomatest.data.feed.FeedRepository
import com.tapan.avomatest.data.model.Feed
import com.tapan.avomatest.data.model.Story
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FakeFeedRepoImpl @Inject constructor() : FeedRepository {
    private val stories = mutableListOf(
        Story("story 1", "https://www.url1.com", isBookMarked = false),
        Story("story 2", "https://www.url2.com", isBookMarked = false),
        Story("story 3", "https://www.url3.com", isBookMarked = false),
    )

    private val feeds = mutableListOf(
        Feed(1, "https://www.feed1.com"),
        Feed(2, "https://www.feed2.com")
    )

    override fun getFeedStories(urls: String): Flow<Resource<List<Story>>> {
        return flow {
            emit(Resource.loading())
            delay(500)
            emit(
                Resource.success(stories)
            )
        }
    }

    override fun getAllFeeds(): Flow<Resource<List<Feed>>> {
        return flow {
            emit(Resource.loading())
            delay(500)
            while (true) {
                emit(
                    Resource.success(feeds)
                )
                delay(1000)
            }

        }
    }

    override fun addFeed(link: String): Flow<Resource<Long>> {
        return flow {
            emit(Resource.loading())
            feeds.add(Feed(feeds.size, link))
            delay(500)
            emit(
                Resource.success(1L)
            )
        }
    }

    override fun deleteFeed(link: String): Flow<Resource<Int>> {
        return flow {
            emit(Resource.loading())
            delay(500)
            feeds.removeIf {
                it.link == link
            }
            emit(
                Resource.success(1)
            )
        }
    }
}