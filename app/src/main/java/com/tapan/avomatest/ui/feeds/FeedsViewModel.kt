package com.tapan.avomatest.ui.feeds

import androidx.lifecycle.ViewModel
import com.tapan.avomatest.data.base.Resource
import com.tapan.avomatest.data.base.Status
import com.tapan.avomatest.data.feed.FeedRepository
import com.tapan.avomatest.data.model.Story
import com.tapan.avomatest.data.story.StoryRepository
import com.tapan.avomatest.launchInBackGround
import com.tapan.avomatest.ui.feeds.states.StoryListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class FeedsViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val storyState = MutableStateFlow(StoryListState())
    fun getStoryState() = storyState as StateFlow<StoryListState>


    fun addRemoveBookMarkStory(story: Story) {
        if (!story.isBookMarked) {
            story.isBookMarked = true
            launchInBackGround(storyState, {
                storyRepository.addBookMark(story)
            }, {
                val bookedMarks = storyState.value.bookMarked.orEmpty().toMutableList()
                bookedMarks.add(story)
                storyState.value.copy(
                    bookMarked = bookedMarks,
                    feeds = storyState.value.feeds.orEmpty().map { story1 ->
                        if (story1.link == story.link) {
                            story1.isBookMarked = true
                        }
                        story1
                    }
                )
            })

        } else {

            story.isBookMarked = false
            launchInBackGround(storyState, {
                storyRepository.removeBookMark(story.link.orEmpty())
            }, {
                storyState.value.copy(
                    bookMarked = storyState.value.feeds?.filter {
                        it.link != story.link
                    },
                    feeds = storyState.value.feeds.orEmpty().map { story1 ->
                        if (story1.link == story.link) {
                            story1.isBookMarked = false
                        }
                        story1
                    }
                )
            })
        }
    }

    fun getFeeds(url: String) {
        launchInBackGround(storyState, {
            storyRepository.getAllStoriesByFeed(url)
                .flatMapLatest {
                    if (it.status == Status.SUCCESS) {
                        storyState.value = StoryListState(bookMarked = it.data)
                        feedRepository.getFeedStories(url)
                    } else {
                        flowOf(Resource.loading())
                    }
                }
        }, {
            val bookmarks = storyState.value.bookMarked.orEmpty().map { story ->
                story.link
            }
            storyState.value.copy(feeds = it.map { story ->
                story.isBookMarked = bookmarks.contains(story.link)
                story
            })
        })

    }
}