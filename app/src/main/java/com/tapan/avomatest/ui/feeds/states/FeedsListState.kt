package com.tapan.avomatest.ui.feeds.states

import com.tapan.avomatest.data.model.Feed
import com.tapan.avomatest.data.model.Story
import com.tapan.avomatest.ui.core.BaseState

data class FeedsListState(
    val feeds: List<Feed>? = null,
) : BaseState<FeedsListState>() {

    override fun equals(other: Any?): Boolean {
        return other is BaseState<*> && other.isLoading == isLoading && super.equals(other)
    }
}