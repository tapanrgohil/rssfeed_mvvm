package com.tapan.avomatest.data.story

import com.tapan.avomatest.data.base.Resource
import com.tapan.avomatest.data.base.getFlow
import com.tapan.avomatest.data.model.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface StoryRepository {

    fun getAllStoriesByFeed(url: String): Flow<Resource<List<Story>>>
    fun getAllStories(): Flow<Resource<List<Story>>>
    fun addBookMark(story: Story): Flow<Resource<Long>>
    fun removeBookMark(link: String): Flow<Resource<Int>>
}

class StoryRepositoryImpl @Inject constructor(
    private val storyLocalSource: StoryLocalSource
) : StoryRepository {
    override fun getAllStoriesByFeed(url: String): Flow<Resource<List<Story>>> {
        return getFlow(local = {
            storyLocalSource.getAllStoriesByLink(url)
                .map {
                    with(it) {
                        Story(title, link, image, publishDate, description, feedUrl, true)
                    }
                }
        })
    }

    override fun getAllStories(): Flow<Resource<List<Story>>> {
        return getFlow(localFlow = { storyLocalSource.getAllStories().map {
            it.map { entity ->
                with(entity) {
                    Story(title, link, image, publishDate, description, feedUrl, true)
                }
            }
        }})
    }

    override fun addBookMark(story: Story): Flow<Resource<Long>> {
        return getFlow(local = { storyLocalSource.addStory(story.toStoryEntity()) })
    }

    override fun removeBookMark(link: String): Flow<Resource<Int>> {
        return getFlow(local = { storyLocalSource.deleteStoryByLink(link) })
    }


}