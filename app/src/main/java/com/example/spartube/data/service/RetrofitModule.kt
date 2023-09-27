package com.example.spartube.data.service

import com.example.spartube.BuildConfig
import com.example.spartube.data.model.categorymodel.ResponseCategory
import com.example.spartube.data.model.videomodel.ResponseModel
import com.example.spartube.data.service.YouTubeSearchService.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit을 통해 인터넷 연결하게 해주는 모듈 (구현체)
object RetrofitModule {
    private fun buildOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                //로깅 인터셉터
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()

    private val youtubeService: YouTubeSearchService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildOkHttpClient())
            .build()
            .create(YouTubeSearchService::class.java)
    }

    suspend fun getVideos(): Response<ResponseModel> {
        return youtubeService.getVideos()
    }

    suspend fun getVideos(category: String?): Response<ResponseModel> {
        return youtubeService.getVideos(categoryId = category)
    }

    suspend fun getVideosByChannel(channelId: String?): Response<ResponseModel> {
        return youtubeService.getVideosByChannel(channelId = channelId)
    }

    suspend fun getCategories(): Response<ResponseCategory> {
        return youtubeService.getVideoCategories()
    }

    suspend fun getShortsVideos(token: String?): Response<ResponseModel> {
        return youtubeService.getShorts(pageToken = token)
    }
}