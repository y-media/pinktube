package com.example.spartube.data.model.categorymodel


import com.google.gson.annotations.SerializedName

data class ResponseCategory(
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("kind")
    val kind: String
)