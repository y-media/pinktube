package com.example.spartube.data.model.commentmodel


import com.google.gson.annotations.SerializedName

data class SnippetXX(
    @SerializedName("authorChannelId")
    val authorChannelId: AuthorChannelId,
    @SerializedName("authorChannelUrl")
    val authorChannelUrl: String,
    @SerializedName("authorDisplayName")
    val authorDisplayName: String,
    @SerializedName("authorProfileImageUrl")
    val authorProfileImageUrl: String,
    @SerializedName("canRate")
    val canRate: Boolean,
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("textDisplay")
    val textDisplay: String,
    @SerializedName("textOriginal")
    val textOriginal: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("videoId")
    val videoId: String,
    @SerializedName("viewerRating")
    val viewerRating: String
)