package com.tapan.avomatest.data.feed

import com.tapan.avomatest.data.base.Resource
import com.tapan.avomatest.data.base.getFlow
import com.tapan.avomatest.data.base.getRemoteFlow
import com.tapan.avomatest.data.exception.StringProvider
import com.tapan.avomatest.data.model.Feed
import com.tapan.avomatest.data.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.contracts.ExperimentalContracts


interface FeedRepository {

    fun getFeedStories(urls: String): Flow<Resource<List<Story>>>
    fun getAllFeeds(): Flow<Resource<List<Feed>>>
    fun addFeed(link: String): Flow<Resource<Long>>
    fun deleteFeed(link: String): Flow<Resource<Int>>
}

@ExperimentalContracts
@Singleton
class FeedRepositoryImpl @Inject constructor(
    private val feedRemoteSource: FeedRemoteSource,
    private val feedLocalSource: FeedLocalSource,
    private val stringProvider: StringProvider
) : FeedRepository {
    override fun getFeedStories(urls: String): Flow<Resource<List<Story>>> {
        return getRemoteFlow({
            feedRemoteSource.getFeedData(urls)
        }, {
            it.items.orEmpty().map {
                with(it) {
                    Story(title, link, image, publishDate, description, urls)
                }
            }
        }, stringProvider)

    }

    override fun getAllFeeds(): Flow<Resource<List<Feed>>> {
        return getFlow({
            feedLocalSource.getAllFeeds().map { value ->
                value.map {
                    with(it) {
                        Feed(index ?: -1, link)
                    }
                }
            }
        })
    }

    override fun addFeed(link: String): Flow<Resource<Long>> {
        return getFlow(local = {
            feedLocalSource.addFeed(link)
        })
    }

    override fun deleteFeed(link: String): Flow<Resource<Int>> {
        return getFlow(local = {
            feedLocalSource.deleteFeed(link)
        })
    }


}

