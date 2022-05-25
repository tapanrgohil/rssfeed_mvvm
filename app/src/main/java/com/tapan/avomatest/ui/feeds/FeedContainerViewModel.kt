package com.tapan.avomatest.ui.feeds

import com.tapan.avomatest.data.feed.FeedRepository
import com.tapan.avomatest.launchInBackGround
import com.tapan.avomatest.ui.core.BaseViewModel
import com.tapan.avomatest.ui.feeds.states.FeedsListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedContainerViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : BaseViewModel() {

    private val feedState = MutableStateFlow(FeedsListState())
    fun getFeedState() = feedState as StateFlow<FeedsListState>


    fun getAllFeed() {
        launchInBackGround(feedState, {
            feedRepository.getAllFeeds()
        }, {
            feedState.value.copy(feeds = it)
        })
    }
}