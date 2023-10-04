package com.example.spartube.data.model.commentmodel


import com.google.gson.annotations.SerializedName

data class SnippetX(
    @SerializedName("canReply")
    val canReply: Boolean,
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("isPublic")
    val isPublic: Boolean,
    @SerializedName("topLevelComment")
    val topLevelComment: TopLevelComment,
    @SerializedName("totalReplyCount")
    val totalReplyCount: Int,
    @SerializedName("videoId")
    val videoId: String
)