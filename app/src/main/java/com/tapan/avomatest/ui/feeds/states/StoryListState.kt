package com.tapan.avomatest.ui.feeds.states

import com.tapan.avomatest.data.model.Story
import com.tapan.avomatest.ui.core.BaseState

data class StoryListState(
    val link:String?=null,
    val feeds: List<Story>? = null,
    val bookMarked:List<Story>?=null
) : BaseState<StoryListState>() {

    override fun equals(other: Any?): Boolean {
        return other is BaseState<*> && other.isLoading == isLoading && super.equals(other)
    }
}