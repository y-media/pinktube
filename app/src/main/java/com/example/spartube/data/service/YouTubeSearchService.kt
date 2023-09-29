package com.example.spartube.data.service

import com.example.spartube.BuildConfig
import com.example.spartube.data.model.categorymodel.ResponseCategory
import com.example.spartube.data.model.commentmodel.ResponseComment
import com.example.spartube.data.model.videomodel.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit Service를 연결하는 인터페이스
interface YouTubeSearchService {
    // http restful
    @GET("v3/videos")
    suspend fun getVideos(
        @Query("key") token: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("chart") query: String? = "mostPopular",
        @Query("videoCategoryId") categoryId: String? = null,
        @Query("regionCode") code: String? = null
    ): Response<ResponseModel>


    @GET("v3/videoCategories")
    suspend fun getVideoCategories(
        @Query("key") token: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet",
        @Query("regionCode") code: String = "KR"
    ): Response<ResponseCategory>

    @GET("v3/channels")
    suspend fun getVideosByChannel(
        @Query("key") token: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet",
        @Query("id") channelId: String? = null
    ): Response<ResponseModel>

    // 여러 개 가져와서 duration이 1분 이하만 필터링
    @GET("v3/videos")
    suspend fun getShorts(
        @Query("key") token: String = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("chart") query: String? = "mostPopular",
        @Query("maxResults") count: Int = 50,
        @Query("pageToken") pageToken: String? = null,
    ): Response<ResponseModel>

    @GET("v3/commentThreads")
    suspend fun getCommentsOfShorts(
        @Query("key") token: String? = BuildConfig.YOUTUBE_API_KEY,
        @Query("part") part: String? = "id,snippet,replies",
        @Query("order") ordering: String? = "relevance",
        @Query("textFormat") format: String? = "plainText",
        @Query("videoId") videoId: String?
    ): Response<ResponseComment>

    companion object {
        const val BASE_URL = "https://www.googleapis.com/youtube/"
    }
}