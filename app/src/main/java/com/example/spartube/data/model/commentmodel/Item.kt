package com.example.spartube.data.model.commentmodel


import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("kind")
    val kind: String,
    @SerializedName("replies")
    val replies: Replies,
    @SerializedName("snippet")
    val snippet: SnippetX
)