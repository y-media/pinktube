package com.example.spartube.data.model.videomodel


import com.google.gson.annotations.SerializedName

data class Statistics(
    @SerializedName("commentCount")
    val commentCount: String,
    @SerializedName("favoriteCount")
    val favoriteCount: String,
    @SerializedName("likeCount")
    val likeCount: String,
    @SerializedName("viewCount")
    val viewCount: String
)