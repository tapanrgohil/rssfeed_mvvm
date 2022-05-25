package com.tapan.avomatest.data.story

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StoryLocalSource {

    fun addStory(storyEntity: StoryEntity): Long
    fun deleteStoryByLink(link: String): Int
    fun getAllStoriesByLink(link: String): List<StoryEntity>
    fun getAllStories(): Flow<List<StoryEntity>>
}

class StoryLocalSourceImpl @Inject constructor(
    private val storyDao: StoryDao
) : StoryLocalSource {
    override fun addStory(storyEntity: StoryEntity): Long {
        return storyDao.insert(storyEntity)
    }

    override fun deleteStoryByLink(link: String): Int {
        return storyDao.deleteFeeByLink(link)
    }

    override fun getAllStoriesByLink(link: String): List<StoryEntity> {
        return storyDao.getAllStoriesByFeed(link)
    }

    override fun getAllStories(): Flow<List<StoryEntity>> {
        return storyDao.getAllStories()
    }

}