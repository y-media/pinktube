package com.example.spartube.data.model.categorymodel


import com.google.gson.annotations.SerializedName

data class Snippet(
    @SerializedName("assignable")
    val assignable: Boolean,
    @SerializedName("channelId")
    val channelId: String,
    @SerializedName("title")
    val title: String
)