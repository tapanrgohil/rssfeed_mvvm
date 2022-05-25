package com.tapan.avomatest.data.feed

import com.tapan.avomatest.data.base.Status
import com.tapan.avomatest.data.exception.StringProvider
import com.tapan.avomatest.data.utils.RssFeed
import com.tapan.avomatest.data.utils.RssItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.contracts.ExperimentalContracts

class FeedRepositoryTest {

    @Mock
    lateinit var feedLocalSource: FeedLocalSource

    @Mock
    lateinit var feedRemoteSource: FeedRemoteSource

    lateinit var feedRepository: FeedRepository

    @Mock
    lateinit var stringProvider: StringProvider

    @ExperimentalContracts
    @Before
    fun setup() {

        MockitoAnnotations.initMocks(this)
        feedRepository = FeedRepositoryImpl(feedRemoteSource, feedLocalSource, stringProvider)

        whenever(stringProvider.messageForStringResource(any())).thenReturn("Error")
        whenever(stringProvider.getErrorCodeMessageRes(any())).thenReturn(500)

    }

    @OptIn(ExperimentalContracts::class)
    @Test
    fun `test getFeeds`() {
        val feedUrl = "https://google.com"
        feedRemoteSource.stub {
            onBlocking {
                getFeedData(feedUrl)
            }.doReturn(Response.success(RssFeed(listOf(RssItem().apply {
                title = "test1"
            }))))
        }
        runBlocking {
            val list = feedRepository.getFeedStories(feedUrl).toList()
            assert(list.first().status == Status.LOADING)
            assert(list[1].status == Status.SUCCESS)
            assert(list[1].data?.firstOrNull()?.title == "test1")

        }
    }

    @OptIn(ExperimentalContracts::class)
    @Test
    fun `test getAllFeeds`() {
        val feedUrl = "https://google.com"

        whenever(feedLocalSource.getAllFeeds())
            .thenReturn(
                flowOf(
                    listOf(FeedEntity(feedUrl))
                )
            )

        runBlocking {
            val list = feedRepository.getAllFeeds().toList()
            assert(list.first().status == Status.LOADING)
            assert(list[1].status == Status.SUCCESS)
            assert(list[1].data?.firstOrNull()?.link == feedUrl)

        }
    }

    @OptIn(ExperimentalContracts::class)
    @Test
    fun `test addFeed`() {
        val feedUrl = "https://google.com"
        val feedEntity = FeedEntity(feedUrl)
        whenever(feedLocalSource.addFeed(feedUrl))
            .thenReturn(1)

        runBlocking {
            val list = feedRepository.addFeed(feedUrl).toList()
            assert(list.first().status == Status.LOADING)
            assert(list[1].status == Status.SUCCESS)
            assert(list[1].data == 1L)

        }
    }

    @Test
    fun `test deleteFeed`() {
        val feedUrl = "https://google.com"
        val feedEntity = FeedEntity(feedUrl)
        whenever(feedLocalSource.deleteFeed(feedUrl))
            .thenReturn(1)

        runBlocking {
            val list = feedRepository.deleteFeed(feedUrl).toList()
            assert(list.first().status == Status.LOADING)
            assert(list[1].status == Status.SUCCESS)
            assert(list[1].data == 1)

        }
    }

}