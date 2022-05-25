package com.tapan.avomatest.data.feed

import com.tapan.avomatest.data.utils.RssFeed
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import javax.inject.Inject
import javax.inject.Singleton

interface FeedRemoteSource {
    suspend fun getFeedData(@Url url: String): Response<RssFeed>
}


@Singleton
class FeedRemoteSourceImpl @Inject constructor(private val networkService: FeedNetworkService) :
    FeedRemoteSource {

    override suspend fun getFeedData(url: String): Response<RssFeed> {
        return networkService.getFeedData(url)
    }


}

interface FeedNetworkService {

    @GET
    suspend fun getFeedData(@Url url: String): Response<RssFeed>

}