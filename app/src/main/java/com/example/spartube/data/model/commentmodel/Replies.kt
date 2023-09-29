package com.example.spartube.data.model.commentmodel


import com.google.gson.annotations.SerializedName

data class Replies(
    @SerializedName("comments")
    val comments: List<Comment>
)