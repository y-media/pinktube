package com.example.spartube.data.model.commentmodel


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("snippet")
    val snippet: Snippet
)