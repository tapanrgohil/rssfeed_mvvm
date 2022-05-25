package com.tapan.avomatest

import com.tapan.avomatest.ui.feeds.FeedListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FeedViewModelTest {
    lateinit var feedListViewModel: FeedListViewModel


    @Before
    fun setup() {
        feedListViewModel = FeedListViewModel(FakeFeedRepoImpl())
    }

    @Test
    fun testAddFeed() {
        runBlocking {
            val url = "https://test.com"
            feedListViewModel.getAllFeed()
            feedListViewModel.addFeed(url)

            delay(2000)
            assert(
                feedListViewModel.getFeedState().value.feeds.orEmpty().any {
                    it.link == url
                }
            )
        }

    }

    @Test
    fun testGetFeed() {
        runBlocking {
            feedListViewModel.getAllFeed()
            delay(2000)
            println(feedListViewModel.getFeedState().value.feeds.orEmpty().firstOrNull()?.link)
            assert(
                feedListViewModel.getFeedState().value.feeds.orEmpty().firstOrNull()?.link
                        == "https://www.feed1.com"
            )
        }

    }

    @Test
    fun testGetAllFeeds() {
        runBlocking {
            val url = "https://test.com"
            feedListViewModel.getAllFeed()
            feedListViewModel.addFeed(url)

            delay(2000)
            assert(
                feedListViewModel.getFeedState().value.feeds.orEmpty().any {
                    it.link == url
                }
            )
        }

    }
}
