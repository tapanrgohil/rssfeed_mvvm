package com.tapan.avomatest.ui.bookmarks

import com.tapan.avomatest.data.model.Story
import com.tapan.avomatest.data.story.StoryRepository
import com.tapan.avomatest.launchInBackGround
import com.tapan.avomatest.ui.core.BaseViewModel
import com.tapan.avomatest.ui.feeds.states.StoryListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : BaseViewModel() {

    private val storyState = MutableStateFlow(StoryListState())
    fun getStoryState() = storyState as StateFlow<StoryListState>


    fun getAllStories() {
        launchInBackGround(storyState, {
            storyRepository.getAllStories()
        }, {
            storyState.value.copy(feeds = it)
        })
    }

    fun removeBookMark(story: Story) {
        launchInBackGround(storyState, {
            storyRepository.removeBookMark(story.link.orEmpty())
        }, {
            storyState.value.copy()
        })

    }


}